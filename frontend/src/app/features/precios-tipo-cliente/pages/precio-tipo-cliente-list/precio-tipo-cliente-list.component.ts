import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { HttpErrorResponse } from '@angular/common/http';
import { Subject, debounceTime, distinctUntilChanged, finalize, forkJoin, takeUntil } from 'rxjs';
import { PermissionService } from '../../../../core/auth/services/permission.service';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ProductService } from '../../../../core/services/product.service';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../../../shared/material/material.module';
import { PrecioTipoClienteResponse } from '../../models/precio-tipo-cliente.model';
import { PrecioTipoClienteService } from '../../services/precio-tipo-cliente.service';

@Component({
  selector: 'app-precio-tipo-cliente-list',
  imports: [ReactiveFormsModule, RouterLink, MaterialModule, PageHeaderComponent, EmptyStateComponent],
  templateUrl: './precio-tipo-cliente-list.component.html',
  styleUrl: './precio-tipo-cliente-list.component.scss',
})
export class PrecioTipoClienteListComponent implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly permissions = inject(PermissionService);
  private readonly notifications = inject(NotificationService);
  private readonly preciosService = inject(PrecioTipoClienteService);
  private readonly productosService = inject(ProductService);
  private readonly catalogosService = inject(CatalogoV1Service);
  private readonly destroy$ = new Subject<void>();

  readonly productos = signal<{ id: number; nombre: string; unidadMedida?: string }[]>([]);
  readonly tiposCliente = signal<{ id: number; descripcion: string }[]>([]);
  readonly estadosProducto = signal<{ id: number; descripcion: string }[]>([]);
  readonly precios = signal<PrecioTipoClienteResponse[]>([]);
  readonly filteredPrecios = signal<PrecioTipoClienteResponse[]>([]);
  readonly pageItems = signal<PrecioTipoClienteResponse[]>([]);

  readonly isLoading = signal(false);
  readonly hasError = signal(false);

  readonly pageSizeOptions = [5, 10, 20, 50];
  readonly pageSize = signal(10);
  readonly pageIndex = signal(0);
  readonly sortState = signal<Sort>({ active: 'producto', direction: 'asc' });

  readonly displayedColumns = [
    'producto',
    'unidadMedida',
    'tipoCliente',
    'precioUnitario',
    'moneda',
    'estado',
    'acciones',
  ];

  readonly filtersForm = this.fb.nonNullable.group({
    search: [''],
    idProducto: ['ALL'],
    idTipoCliente: ['ALL'],
    idEstadoProducto: ['ALL'],
    moneda: ['ALL'],
  });

  readonly canManagePrices = computed(() => this.permissions.canEditPrices());

  ngOnInit(): void {
    this.bindFilters();
    this.loadCatalogs();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.updatePageItems();
  }

  onSortChange(sort: Sort): void {
    this.sortState.set(sort);
    this.applyLocalFilters();
  }

  clearFilters(): void {
    this.filtersForm.patchValue(
      {
        search: '',
        idProducto: 'ALL',
        idTipoCliente: 'ALL',
        idEstadoProducto: 'ALL',
        moneda: 'ALL',
      },
      { emitEvent: false },
    );
    this.sortState.set({ active: 'producto', direction: 'asc' });
    this.loadPrices();
  }

  getProductLabel(item: PrecioTipoClienteResponse): string {
    return item.producto?.trim() || this.resolveProductName(item.idProducto);
  }

  getProductUnit(item: PrecioTipoClienteResponse): string {
    return item.unidadMedida?.trim() || this.resolveProductUnit(item.idProducto) || '-';
  }

  getTipoClienteLabel(item: PrecioTipoClienteResponse): string {
    return item.tipoCliente?.trim() || item.descTipoCliente?.trim() || this.resolveTipoClienteName(item.idTipoCliente);
  }

  getEstadoLabel(item: PrecioTipoClienteResponse): string {
    return item.estadoProducto?.trim() || item.descEstadoProducto?.trim() || this.resolveEstadoName(item.idEstadoProducto);
  }

  getEstadoTone(item: PrecioTipoClienteResponse): 'ok' | 'warn' | 'danger' | '' {
    const id = item.idEstadoProducto;
    if (id === 1) {
      return 'ok';
    }
    if (id === 2) {
      return 'warn';
    }
    if (id === 3) {
      return 'danger';
    }
    const label = this.normalize(this.getEstadoLabel(item));
    if (label.includes('activo') || label.includes('habilitado')) {
      return 'ok';
    }
    if (label.includes('inactivo') || label.includes('deshabilitado')) {
      return 'warn';
    }
    if (label.includes('bloqueado')) {
      return 'danger';
    }
    return '';
  }

  getCurrencyLabel(moneda: string): string {
    const value = this.normalize(moneda);
    if (value.includes('dolar')) {
      return 'Dolares';
    }
    return 'Soles';
  }

  getCurrencyTone(moneda: string): 'ok' | 'warn' | '' {
    const value = this.normalize(moneda);
    if (value.includes('dolar')) {
      return 'warn';
    }
    return 'ok';
  }

  formatMoney(value: number, moneda: string): string {
    const formatted = new Intl.NumberFormat('es-PE', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(value ?? 0);
    return `${this.currencySymbol(moneda)} ${formatted}`;
  }

  filterByProduct(item: PrecioTipoClienteResponse): void {
    this.filtersForm.patchValue({ idProducto: String(item.idProducto) }, { emitEvent: false });
    this.loadPrices();
  }

  navigateCreate(): void {
    this.router.navigate(['/precios-tipo-cliente/nuevo']);
  }

  private bindFilters(): void {
    this.filtersForm.controls.search.valueChanges
      .pipe(debounceTime(250), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => this.applyLocalFilters());

    this.filtersForm.controls.moneda.valueChanges
      .pipe(debounceTime(150), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => this.applyLocalFilters());

    this.filtersForm.controls.idProducto.valueChanges
      .pipe(debounceTime(150), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => this.loadPrices());

    this.filtersForm.controls.idTipoCliente.valueChanges
      .pipe(debounceTime(150), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => this.loadPrices());

    this.filtersForm.controls.idEstadoProducto.valueChanges
      .pipe(debounceTime(150), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => this.loadPrices());
  }

  private loadCatalogs(): void {
    this.isLoading.set(true);
    this.hasError.set(false);

    forkJoin({
      productos: this.productosService.getProducts(),
      tiposCliente: this.catalogosService.tiposClienteActivos(),
      estadosProducto: this.catalogosService.estadosProducto(),
    })
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: ({ productos, tiposCliente, estadosProducto }) => {
          this.productos.set(
            productos.map((item) => ({
              id: item.id,
              nombre: item.nombre,
              unidadMedida: item.unidadMedida,
            })),
          );
          this.tiposCliente.set(tiposCliente.map((item) => ({ id: item.id, descripcion: item.descripcion })));
          this.estadosProducto.set(estadosProducto.map((item) => ({ id: item.id, descripcion: item.descripcion })));
          this.restoreQueryFilters();
          this.loadPrices();
        },
        error: (error: HttpErrorResponse) => {
          this.hasError.set(true);
          this.notifications.error(error.error?.message ?? 'No se pudo cargar la informacion inicial de precios.');
        },
      });
  }

  private restoreQueryFilters(): void {
    const params = this.route.snapshot.queryParamMap;
    const idProducto = this.toNumber(params.get('producto'));
    const idTipoCliente = this.toNumber(params.get('tipoCliente'));
    const idEstadoProducto = this.toNumber(params.get('estado'));
    const moneda = params.get('moneda');
    const search = params.get('search') ?? '';

    this.filtersForm.patchValue(
      {
        search,
        idProducto: idProducto ? String(idProducto) : 'ALL',
        idTipoCliente: idTipoCliente ? String(idTipoCliente) : 'ALL',
        idEstadoProducto: idEstadoProducto ? String(idEstadoProducto) : 'ALL',
        moneda: moneda && moneda.trim() ? moneda : 'ALL',
      },
      { emitEvent: false },
    );
  }

  private loadPrices(): void {
    this.isLoading.set(true);
    this.hasError.set(false);

    const raw = this.filtersForm.getRawValue();
    const serverFilters = {
      idProducto: this.toNullableNumber(raw.idProducto),
      idTipoCliente: this.toNullableNumber(raw.idTipoCliente),
      idEstadoProducto: this.toNullableNumber(raw.idEstadoProducto),
    };

    const request$ = this.hasServerFilters(serverFilters)
      ? this.preciosService.buscarPrecios(serverFilters)
      : this.preciosService.getPrecios();

    request$.pipe(finalize(() => this.isLoading.set(false)), takeUntil(this.destroy$)).subscribe({
      next: (items) => {
        this.precios.set(items);
        this.applyLocalFilters();
      },
      error: (error: HttpErrorResponse) => {
        this.hasError.set(true);
        this.notifications.error(error.error?.message ?? 'No se pudo cargar la lista de precios.');
      },
    });
  }

  private applyLocalFilters(): void {
    const raw = this.filtersForm.getRawValue();
    const search = this.normalize(raw.search);
    const moneda = raw.moneda;

    const filtered = this.sortPrices(
      this.precios().filter((item) => {
        if (moneda !== 'ALL' && this.normalize(item.moneda) !== this.normalize(moneda)) {
          return false;
        }
        if (search && !this.buildSearchIndex(item).includes(search)) {
          return false;
        }
        return true;
      }),
    );

    this.filteredPrecios.set(filtered);
    this.pageIndex.set(0);
    this.updatePageItems();
  }

  private updatePageItems(): void {
    const start = this.pageIndex() * this.pageSize();
    const end = start + this.pageSize();
    this.pageItems.set(this.filteredPrecios().slice(start, end));
  }

  private sortPrices(items: PrecioTipoClienteResponse[]): PrecioTipoClienteResponse[] {
    const { active, direction } = this.sortState();
    if (!direction) {
      return items;
    }

    const factor = direction === 'asc' ? 1 : -1;
    const collator = new Intl.Collator('es', { sensitivity: 'base', numeric: true });

    return [...items].sort((left, right) => factor * this.compareByActive(left, right, active, collator));
  }

  private compareByActive(
    left: PrecioTipoClienteResponse,
    right: PrecioTipoClienteResponse,
    active: string,
    collator: Intl.Collator,
  ): number {
    switch (active) {
      case 'producto':
        return collator.compare(this.getProductLabel(left), this.getProductLabel(right));
      case 'unidadMedida':
        return collator.compare(this.getProductUnit(left), this.getProductUnit(right));
      case 'tipoCliente':
        return collator.compare(this.getTipoClienteLabel(left), this.getTipoClienteLabel(right));
      case 'precioUnitario':
        return (left.precioUnitario ?? 0) - (right.precioUnitario ?? 0);
      case 'moneda':
        return collator.compare(left.moneda ?? '', right.moneda ?? '');
      case 'estado':
        return collator.compare(this.getEstadoLabel(left), this.getEstadoLabel(right));
      case 'fecActualiza':
        return this.compareDates(left.fecActualiza, right.fecActualiza) || collator.compare(
          this.formatMoney(left.precioUnitario, left.moneda),
          this.formatMoney(right.precioUnitario, right.moneda),
        );
      default:
        return collator.compare(String(left.idPrecio), String(right.idPrecio));
    }
  }

  private compareDates(left?: string, right?: string): number {
    const leftDate = left ? new Date(left).getTime() : 0;
    const rightDate = right ? new Date(right).getTime() : 0;
    return leftDate - rightDate;
  }

  private buildSearchIndex(item: PrecioTipoClienteResponse): string {
    return [
      this.getProductLabel(item),
      this.getProductUnit(item),
      this.getTipoClienteLabel(item),
      this.getEstadoLabel(item),
      item.moneda,
      String(item.precioUnitario ?? ''),
      item.usuRegistro ?? '',
      item.usuActualiza ?? '',
    ]
      .join(' ')
      .toLowerCase();
  }

  private hasServerFilters(filters: { idProducto: number | null; idTipoCliente: number | null; idEstadoProducto: number | null }): boolean {
    return filters.idProducto !== null || filters.idTipoCliente !== null || filters.idEstadoProducto !== null;
  }

  private toNumber(value: string | null): number | null {
    if (!value || value === 'ALL') {
      return null;
    }
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : null;
  }

  private toNullableNumber(value: string): number | null {
    return this.toNumber(value);
  }

  private resolveProductName(idProducto: number): string {
    return this.productos().find((item) => item.id === idProducto)?.nombre ?? `Producto #${idProducto}`;
  }

  private resolveProductUnit(idProducto: number): string | undefined {
    return this.productos().find((item) => item.id === idProducto)?.unidadMedida;
  }

  private resolveTipoClienteName(idTipoCliente: number): string {
    return this.tiposCliente().find((item) => item.id === idTipoCliente)?.descripcion ?? `Tipo #${idTipoCliente}`;
  }

  private resolveEstadoName(idEstadoProducto: number): string {
    return this.estadosProducto().find((item) => item.id === idEstadoProducto)?.descripcion ?? `Estado #${idEstadoProducto}`;
  }

  private currencySymbol(moneda: string): string {
    const normalized = this.normalize(moneda);
    if (normalized.includes('dolar')) {
      return 'US$';
    }
    return 'S/';
  }

  private normalize(value?: string): string {
    return (value ?? '')
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }
}
