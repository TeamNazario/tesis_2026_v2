import { Component, OnDestroy, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpErrorResponse } from '@angular/common/http';
import { Subject, catchError, debounceTime, distinctUntilChanged, filter, finalize, forkJoin, of, switchMap, takeUntil, tap } from 'rxjs';
import { UsuarioResponse } from '../../../../core/auth/models/auth.models';
import { ClienteCreateRequest, ClienteResponseVm, ClienteUpdateRequest } from '../../../../core/models/cliente.models';
import { RucConsultaResponse } from '../../../../core/models/documento.models';
import { CatalogoItem } from '../../../../core/models/v1.models';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { DocumentoService } from '../../../../core/services/documento.service';
import { DomainApiService } from '../../../../core/services/domain-api.service';
import { MaterialModule } from '../../../../shared/material/material.module';

interface ClienteFormDialogData {
  mode: 'create' | 'edit';
  cliente?: ClienteResponseVm;
}

@Component({
  selector: 'app-cliente-form-dialog',
  imports: [ReactiveFormsModule, MaterialModule],
  templateUrl: './cliente-form-dialog.component.html',
  styleUrl: './cliente-form-dialog.component.scss',
})
export class ClienteFormDialogComponent implements OnDestroy {
  readonly data = inject<ClienteFormDialogData>(MAT_DIALOG_DATA);
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<ClienteFormDialogComponent>);
  private readonly documentoService = inject(DocumentoService);
  private readonly catalogos = inject(CatalogoV1Service);
  private readonly domainApi = inject(DomainApiService);
  private readonly destroy$ = new Subject<void>();
  private lastQueriedRuc = '';
  private readonly vendedorPerfilId = 4;
  private readonly usuarioHabilitadoEstadoId = 1;

  readonly tiposCliente = signal<CatalogoItem[]>([]);
  readonly estadosClienteContacto = signal<CatalogoItem[]>([]);
  readonly vendedores = signal<UsuarioResponse[]>([]);
  readonly isLoadingCatalogos = signal(false);
  readonly catalogoError = signal<string | null>(null);
  readonly vendedoresError = signal<string | null>(null);
  readonly estadoActivoError = signal<string | null>(null);
  isConsultandoRuc = false;
  rucInfoMessage: string | null = null;
  rucErrorMessage: string | null = null;
  rucAptoMensaje: string | null = null;

  readonly form = this.fb.nonNullable.group({
    ruc: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
    razonSocial: ['', [Validators.required, Validators.maxLength(200)]],
    idTipoCliente: [0, [Validators.required, Validators.min(1)]],
    idVendedorAsignado: [0, [Validators.required, Validators.min(1)]],
    direccionFiscal: [''],
    departamento: [''],
    provincia: [''],
    distrito: [''],
    condicionSunat: ['HABIDO', [Validators.required]],
    estadoSunat: ['ACTIVO', [Validators.required]],
    ubigeo: ['', [Validators.maxLength(6)]],
    idEstadoClienteContacto: [0, [Validators.required, Validators.min(1)]],
  });

  constructor() {
    this.loadCatalogos();

    if (this.data.cliente) {
      this.form.patchValue({
        ruc: this.data.cliente.ruc,
        razonSocial: this.data.cliente.razonSocial,
        idTipoCliente: this.data.cliente.idTipoCliente ?? 0,
        idVendedorAsignado: this.data.cliente.idVendedorAsignado ?? 0,
        direccionFiscal: this.data.cliente.direccionFiscal ?? '',
        departamento: this.data.cliente.departamento ?? '',
        provincia: this.data.cliente.provincia ?? '',
        distrito: this.data.cliente.distrito ?? '',
        condicionSunat: this.data.cliente.condicionSunat ?? 'HABIDO',
        estadoSunat: this.data.cliente.estadoSunat ?? 'ACTIVO',
        ubigeo: this.data.cliente.ubigeo ?? '',
        idEstadoClienteContacto: this.data.cliente.estado?.id ?? 0,
      });
    }

    this.bindRucAutoLookup();
  }

  get isCreateMode(): boolean {
    return this.data.mode === 'create';
  }

  get estadoClienteNombre(): string {
    const selectedId = this.form.controls.idEstadoClienteContacto.value;
    if (!selectedId) {
      return 'Sin estado';
    }
    return this.estadosClienteContacto().find((item) => item.id === selectedId)?.descripcion ?? 'Sin estado';
  }

  get canSubmit(): boolean {
    if (this.isLoadingCatalogos()) {
      return false;
    }
    return !this.catalogoError() && !this.vendedoresError() && !this.estadoActivoError();
  }

  private bindRucAutoLookup(): void {
    this.form.controls.ruc.valueChanges
      .pipe(
        debounceTime(350),
        distinctUntilChanged(),
        tap((rawValue) => {
          const currentValue = (rawValue ?? '').trim();
          this.resetRucMessages();
          if (currentValue.length < 11) {
            this.lastQueriedRuc = '';
          }
        }),
        filter((rawValue) => /^\d{11}$/.test((rawValue ?? '').trim())),
        filter((rawValue) => {
          const sanitized = (rawValue ?? '').trim();
          return sanitized !== this.lastQueriedRuc;
        }),
        tap(() => {
          this.isConsultandoRuc = true;
          this.rucInfoMessage = 'Consultando datos del RUC...';
        }),
        switchMap((rawValue) => {
          const ruc = (rawValue ?? '').trim();
          this.lastQueriedRuc = ruc;
          return this.documentoService.consultarRuc(ruc).pipe(
            catchError((error: HttpErrorResponse) => {
              this.handleRucLookupError(error);
              return of(null);
            }),
            finalize(() => (this.isConsultandoRuc = false)),
          );
        }),
        takeUntil(this.destroy$),
      )
      .subscribe((response) => {
        if (!response) {
          return;
        }
        this.applyRucResponse(response);
      });
  }

  private applyRucResponse(response: RucConsultaResponse): void {
    this.form.patchValue({
      ruc: response.ruc ?? this.form.controls.ruc.value,
      razonSocial: response.razonSocial ?? '',
      condicionSunat: response.condicion ?? '',
      estadoSunat: response.estado ?? '',
      direccionFiscal: response.direccion ?? '',
      departamento: response.departamento ?? '',
      provincia: response.provincia ?? '',
      distrito: response.distrito ?? '',
      ubigeo: response.ubigeo ?? '',
    });

    this.rucInfoMessage = 'Datos del cliente encontrados y completados automaticamente.';
    this.rucErrorMessage = null;

    if (response.aptoParaCotizacion === true) {
      this.rucAptoMensaje = response.mensajeValidacion ?? 'RUC valido para cotizacion automatica.';
      return;
    }

    if (response.aptoParaCotizacion === false) {
      this.rucAptoMensaje =
        response.mensajeValidacion ??
        'Este RUC no cumple las condiciones recomendadas para cotizacion automatica. Revise la informacion antes de continuar.';
      return;
    }

    this.rucAptoMensaje = null;
  }

  private handleRucLookupError(error: HttpErrorResponse): void {
    this.rucInfoMessage = null;
    this.rucAptoMensaje = null;

    if (error.status === 404) {
      this.rucErrorMessage = 'No se encontraron datos para el RUC ingresado. Completa la informacion manualmente.';
      return;
    }

    if (error.status === 400) {
      this.rucErrorMessage = error.error?.message ?? 'El RUC ingresado no es valido para consulta.';
      return;
    }

    this.rucErrorMessage = 'No fue posible consultar el RUC en este momento. Puedes continuar el registro manualmente.';
  }

  private resetRucMessages(): void {
    this.rucInfoMessage = null;
    this.rucErrorMessage = null;
    this.rucAptoMensaje = null;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get title(): string {
    return this.data.mode === 'create' ? 'Nuevo cliente' : 'Actualizar cliente';
  }

  submit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid || !this.canSubmit) {
      return;
    }
    const value = this.form.getRawValue();
    const payload: ClienteCreateRequest | ClienteUpdateRequest = {
      ruc: value.ruc,
      razonSocial: value.razonSocial,
      idTipoCliente: value.idTipoCliente,
      idVendedorAsignado: value.idVendedorAsignado,
      direccionFiscal: value.direccionFiscal,
      departamento: value.departamento,
      provincia: value.provincia,
      distrito: value.distrito,
      condicionSunat: value.condicionSunat,
      estadoSunat: value.estadoSunat,
      ubigeo: value.ubigeo,
      idEstadoClienteContacto: value.idEstadoClienteContacto,
    };
    this.dialogRef.close(payload);
  }

  cancel(): void {
    this.dialogRef.close();
  }

  private loadCatalogos(): void {
    this.isLoadingCatalogos.set(true);
    this.catalogoError.set(null);
    this.vendedoresError.set(null);
    this.estadoActivoError.set(null);

    forkJoin({
      tiposCliente: this.catalogos.tiposCliente().pipe(
        catchError(() => {
          this.catalogoError.set('No se pudieron cargar los tipos de cliente.');
          return of([] as CatalogoItem[]);
        }),
      ),
      estadosClienteContacto: this.catalogos.estadosClienteContacto().pipe(
        catchError(() => {
          this.estadoActivoError.set('No se pudo determinar el estado inicial del cliente.');
          return of([] as CatalogoItem[]);
        }),
      ),
      vendedores: this.domainApi.getUsuarios().pipe(
        catchError(() => {
          this.vendedoresError.set('No se pudieron cargar los vendedores disponibles.');
          return of([] as UsuarioResponse[]);
        }),
      ),
    })
      .pipe(
        finalize(() => this.isLoadingCatalogos.set(false)),
        takeUntil(this.destroy$),
      )
      .subscribe({
        next: ({ tiposCliente, estadosClienteContacto, vendedores }) => {
          this.tiposCliente.set(tiposCliente);
          this.estadosClienteContacto.set(estadosClienteContacto);
          this.vendedores.set(this.filterVendedores(vendedores));
          this.applyDefaultCatalogValues(tiposCliente, estadosClienteContacto, this.vendedores());
        },
      });
  }

  private applyDefaultCatalogValues(
    tiposCliente: CatalogoItem[],
    estadosClienteContacto: CatalogoItem[],
    vendedores: UsuarioResponse[],
  ): void {
    if (!tiposCliente.length) {
      this.catalogoError.set('No existen tipos de cliente activos configurados.');
    }

    if (!vendedores.length) {
      this.vendedoresError.set('No existen vendedores habilitados para asignar al cliente.');
    }

    if (!this.form.controls.idTipoCliente.value && tiposCliente[0]) {
      this.form.controls.idTipoCliente.setValue(tiposCliente[0].id);
    }

    const estadoActivo = this.findEstadoActivo(estadosClienteContacto);
    if (!estadoActivo) {
      this.estadoActivoError.set('No se pudo determinar el estado inicial del cliente.');
    } else if (!this.form.controls.idEstadoClienteContacto.value || this.isCreateMode) {
      this.form.controls.idEstadoClienteContacto.setValue(estadoActivo.id);
    }

    if (!this.form.controls.idVendedorAsignado.value && vendedores[0]) {
      this.form.controls.idVendedorAsignado.setValue(vendedores[0].idUsuario);
    }
  }

  private filterVendedores(usuarios: UsuarioResponse[]): UsuarioResponse[] {
    const cleaned = usuarios.filter((usuario) => !!(usuario.nombres ?? '').trim());

    // Regla principal (según tu BD): perfil=4 (Vendedor) y estado=1 (Habilitado).
    const porId = cleaned.filter(
      (usuario) => usuario.perfil?.id === this.vendedorPerfilId && usuario.estado?.id === this.usuarioHabilitadoEstadoId,
    );
    if (porId.length) {
      this.vendedoresError.set(null);
      return porId;
    }

    // Respaldo por nombre, por si cambian IDs o catálogos.
    const porNombre = cleaned.filter((usuario) => {
      const perfil = (usuario.perfil?.nombre ?? '').trim().toLowerCase();
      const estado = (usuario.estado?.nombre ?? '').trim().toLowerCase();
      const isVendedor = perfil.includes('vendedor');
      const isHabilitado = estado.includes('habilitado') || estado.includes('activo');
      return isVendedor && isHabilitado;
    });
    if (porNombre.length) {
      this.vendedoresError.set(null);
      return porNombre;
    }

    // Último fallback: al menos mostrar usuarios con perfil vendedor aunque estado no venga mapeado como esperamos.
    const soloPerfil = cleaned.filter((usuario) => usuario.perfil?.id === this.vendedorPerfilId);
    if (soloPerfil.length) {
      this.vendedoresError.set('No se pudo filtrar por estado habilitado. Mostrando vendedores por perfil.');
      return soloPerfil;
    }

    this.vendedoresError.set('No existen vendedores habilitados para asignar al cliente.');
    return [];
  }

  private findEstadoActivo(estadosClienteContacto: CatalogoItem[]): CatalogoItem | undefined {
    return estadosClienteContacto.find((item) => item.descripcion.trim().toLowerCase() === 'activo');
  }
}
