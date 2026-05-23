import { Component, OnDestroy, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpErrorResponse } from '@angular/common/http';
import { Subject, catchError, debounceTime, distinctUntilChanged, filter, finalize, of, switchMap, takeUntil, tap } from 'rxjs';
import { ClienteCreateRequest, ClienteResponseVm, ClienteUpdateRequest } from '../../../../core/models/cliente.models';
import { RucConsultaResponse } from '../../../../core/models/documento.models';
import { DocumentoService } from '../../../../core/services/documento.service';
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
  private readonly destroy$ = new Subject<void>();
  private lastQueriedRuc = '';

  isConsultandoRuc = false;
  rucInfoMessage: string | null = null;
  rucErrorMessage: string | null = null;
  rucAptoMensaje: string | null = null;

  readonly form = this.fb.nonNullable.group({
    ruc: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
    razonSocial: ['', [Validators.required, Validators.maxLength(200)]],
    nombreComercial: ['', [Validators.maxLength(200)]],
    tipoCliente: ['Empresa'],
    direccionFiscal: [''],
    zonaDespacho: [''],
    departamento: [''],
    provincia: [''],
    distrito: [''],
    telefonoPrincipal: ['', [Validators.pattern(/^\+?\d{6,15}$/)]],
    correoPrincipal: ['', [Validators.email]],
    observaciones: ['', [Validators.maxLength(500)]],
    condicionSunat: ['HABIDO', [Validators.required]],
    estadoSunat: ['ACTIVO', [Validators.required]],
    ubigeo: ['', [Validators.maxLength(6)]],
    estadoId: [1, [Validators.required]],
  });

  constructor() {
    if (this.data.cliente) {
      this.form.patchValue({
        ruc: this.data.cliente.ruc,
        razonSocial: this.data.cliente.razonSocial,
        nombreComercial: this.data.cliente.nombreComercial ?? '',
        tipoCliente: this.data.cliente.tipoCliente ?? 'Empresa',
        direccionFiscal: this.data.cliente.direccionFiscal ?? '',
        zonaDespacho: this.data.cliente.zonaDespacho ?? '',
        departamento: this.data.cliente.departamento ?? '',
        provincia: this.data.cliente.provincia ?? '',
        distrito: this.data.cliente.distrito ?? '',
        telefonoPrincipal: this.data.cliente.telefonoPrincipal ?? '',
        correoPrincipal: this.data.cliente.correoPrincipal ?? '',
        observaciones: this.data.cliente.observaciones ?? '',
        condicionSunat: this.data.cliente.condicionSunat ?? 'HABIDO',
        estadoSunat: this.data.cliente.estadoSunat ?? 'ACTIVO',
        ubigeo: this.data.cliente.ubigeo ?? '',
        estadoId: this.data.cliente.estado?.id ?? 1,
      });
    }

    this.bindRucAutoLookup();
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
    if (this.form.invalid) {
      return;
    }
    const value = this.form.getRawValue();
    const payload: ClienteCreateRequest | ClienteUpdateRequest = {
      ruc: value.ruc,
      razonSocial: value.razonSocial,
      nombreComercial: value.nombreComercial,
      tipoCliente: value.tipoCliente,
      direccionFiscal: value.direccionFiscal,
      zonaDespacho: value.zonaDespacho,
      departamento: value.departamento,
      provincia: value.provincia,
      distrito: value.distrito,
      telefonoPrincipal: value.telefonoPrincipal,
      correoPrincipal: value.correoPrincipal,
      observaciones: value.observaciones,
      condicionSunat: value.condicionSunat,
      estadoSunat: value.estadoSunat,
      ubigeo: value.ubigeo,
      estadoId: value.estadoId,
    };
    this.dialogRef.close(payload);
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
