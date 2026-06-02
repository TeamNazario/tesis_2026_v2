import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { Subject, debounceTime, distinctUntilChanged, finalize, takeUntil } from 'rxjs';
import { ProductFilter, ProductResponse, ProductUpdateRequest } from '../../core/models/product.models';
import { NotificationService } from '../../core/services/notification.service';
import { ProductService } from '../../core/services/product.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../shared/material/material.module';
import { ConfirmStatusDialogComponent } from './components/confirm-status-dialog/confirm-status-dialog.component';
import { ProductoDetailDialogComponent } from './components/producto-detail-dialog/producto-detail-dialog.component';
import { ProductoFormDialogComponent } from './components/producto-form-dialog/producto-form-dialog.component';

@Component({
  selector: 'app-catalogo-productos',
  imports: [ReactiveFormsModule, MaterialModule, PageHeaderComponent, EmptyStateComponent],
  templateUrl: './catalogo-productos.component.html',
  styleUrl: './catalogo-productos.component.scss',
})
export class CatalogoProductosComponent implements OnInit, OnDestroy {
  readonly api = inject(ProductService);
  private readonly fb = inject(FormBuilder);
  private readonly notifications = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroy$ = new Subject<void>();

  readonly products = signal<ProductResponse[]>([]);
  readonly filteredProducts = signal<ProductResponse[]>([]);
  readonly pageItems = signal<ProductResponse[]>([]);

  readonly isLoading = signal(false);
  readonly isSaving = signal(false);
  readonly hasError = signal(false);

  readonly displayedColumns = [
    'codigo',
    'nombre',
    'presentacion',
    'unidadMedida',
    'cantMinVenta',
    'stockDisponible',
    'estado',
    'acciones',
  ];

  readonly pageSizeOptions = [5, 10, 20, 50];
  readonly pageSize = signal(10);
  readonly pageIndex = signal(0);

  readonly filterForm = this.fb.nonNullable.group({
    search: [''],
    estado: ['ALL'],
    presentacion: [''],
    unidadMedida: [''],
    stockBajo: [false],
  });

  readonly uniquePresentaciones = computed(() =>
    Array.from(new Set(this.products().map((item) => item.presentacion).filter((value): value is string => !!value))).sort(),
  );

  readonly uniqueUnidades = computed(() => Array.from(new Set(this.products().map((item) => item.unidadMedida))).sort());
  readonly enabledCount = computed(() => this.products().filter((item) => this.api.isActive(item)).length);
  readonly lowStockCount = computed(() => this.products().filter((item) => this.getStockBadge(item) !== 'ok').length);
  readonly totalStockDisponible = computed(() => this.products().reduce((total, item) => total + item.stockDisponible, 0));

  ngOnInit(): void {
    this.loadProducts();

    this.filterForm.controls.search.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => this.applyFilters());

    this.filterForm.valueChanges.pipe(debounceTime(150), takeUntil(this.destroy$)).subscribe(() => this.applyFilters());
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

  clearFilters(): void {
    this.filterForm.setValue({
      search: '',
      estado: 'ALL',
      presentacion: '',
      unidadMedida: '',
      stockBajo: false,
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(ProductoFormDialogComponent, {
      width: '980px',
      maxWidth: '95vw',
      data: { mode: 'create' },
    });

    dialogRef.afterClosed().subscribe((payload) => {
      if (!payload) {
        return;
      }
      this.isSaving.set(true);
      this.api
        .createProduct(payload)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('Producto registrado correctamente.');
            this.loadProducts();
          },
          error: (error) => this.handleMutationError(error),
        });
    });
  }

  openEditDialog(product: ProductResponse): void {
    const dialogRef = this.dialog.open(ProductoFormDialogComponent, {
      width: '980px',
      maxWidth: '95vw',
      data: { mode: 'edit', product },
    });

    dialogRef.afterClosed().subscribe((payload: ProductUpdateRequest | undefined) => {
      if (!payload) {
        return;
      }
      this.isSaving.set(true);
      this.api
        .updateProduct(product.id, payload)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('Producto actualizado correctamente.');
            this.loadProducts();
          },
          error: (error) => this.handleMutationError(error),
        });
    });
  }

  openDetail(product: ProductResponse): void {
    this.dialog.open(ProductoDetailDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: product,
    });
  }

  toggleStatus(product: ProductResponse): void {
    const isActive = this.api.isActive(product);
    const nextStatusId = isActive ? 2 : 1;
    const nextStatusLabel = isActive ? 'Inhabilitado' : 'Habilitado';

    const dialogRef = this.dialog.open(ConfirmStatusDialogComponent, {
      width: '520px',
      maxWidth: '95vw',
      data: { productName: product.nombre, nextStatusLabel },
    });

    dialogRef.afterClosed().subscribe((confirmed) => {
      if (!confirmed) {
        return;
      }
      this.isSaving.set(true);
      this.api
        .changeProductStatus(product.id, nextStatusId)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('El estado del producto fue actualizado correctamente.');
            this.loadProducts();
          },
          error: (error) => this.handleMutationError(error),
        });
    });
  }

  getStatusLabel(product: ProductResponse): string {
    return this.api.getStatusLabel(product);
  }

  isEnabled(product: ProductResponse): boolean {
    return product.estado?.id === 1;
  }

  isBlocked(product: ProductResponse): boolean {
    return this.api.isBlocked(product);
  }

  getStatusClass(product: ProductResponse): string {
    if (this.api.isBlocked(product)) {
      return 'blocked';
    }
    if (this.api.isInactive(product)) {
      return 'inactive';
    }
    return this.api.isActive(product) ? 'active' : 'unknown';
  }

  getStockBadge(product: ProductResponse): 'critical' | 'warn' | 'ok' {
    if (product.stockDisponible <= 0) {
      return 'critical';
    }
    if (product.stockDisponible <= product.stockMinimo) {
      return 'warn';
    }
    return 'ok';
  }

  getStockLabel(product: ProductResponse): string {
    const badge = this.getStockBadge(product);
    if (badge === 'critical') {
      return 'Sin stock';
    }
    if (badge === 'warn') {
      return 'Stock bajo';
    }
    return 'Stock ok';
  }

  private loadProducts(): void {
    this.isLoading.set(true);
    this.hasError.set(false);

    this.api
      .getProducts()
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (products) => {
          this.products.set(products);
          this.applyFilters();
        },
        error: () => {
          this.hasError.set(true);
        },
      });
  }

  private applyFilters(): void {
    const raw = this.filterForm.getRawValue();
    const filter: ProductFilter = {
      search: raw.search ?? '',
      estado: (raw.estado as ProductFilter['estado']) ?? 'ALL',
      presentacion: raw.presentacion ?? '',
      unidadMedida: raw.unidadMedida ?? '',
      stockBajo: raw.stockBajo ?? false,
      page: this.pageIndex(),
      size: this.pageSize(),
      sort: 'nombre',
      direction: 'asc',
    };

    const filtered = this.api.applyLocalSearch(this.products(), filter.search, filter);
    this.filteredProducts.set(filtered);
    this.pageIndex.set(0);
    this.updatePageItems();
  }

  private updatePageItems(): void {
    const start = this.pageIndex() * this.pageSize();
    const end = start + this.pageSize();
    this.pageItems.set(this.filteredProducts().slice(start, end));
  }

  private handleMutationError(error: HttpErrorResponse): void {
    if (error.status === 400 && error.error?.fields) {
      this.notifications.error('Existen errores de validacion en el formulario. Revisa los campos requeridos.');
      return;
    }
    if (error.status === 404) {
      this.notifications.error('El producto solicitado ya no existe o no fue encontrado.');
      return;
    }
    if (error.status === 409) {
      this.notifications.error('No se pudo guardar por conflicto de datos. Verifica el estado del producto.');
      return;
    }
    this.notifications.error(error.error?.message ?? 'No se pudo completar la operacion con productos.');
  }
}
