import { Injectable, inject } from '@angular/core';
import { Observable, map, switchMap } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { ProductoResponse } from '../models/domain.models';
import {
  ProductCreateRequest,
  ProductFilter,
  ProductResponse as ProductVm,
  ProductUpdateRequest,
} from '../models/product.models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly api = inject(ApiService);

  getProducts(): Observable<ProductVm[]> {
    return this.api.get<ProductoResponse[]>(API_ENDPOINTS.productos).pipe(map((items) => items.map((item) => this.toVm(item))));
  }

  getProductsByEstado(estadoId: number): Observable<ProductVm[]> {
    return this.api
      .get<ProductoResponse[]>(`${API_ENDPOINTS.productos}/estado/${estadoId}`)
      .pipe(map((items) => items.map((item) => this.toVm(item))));
  }

  getProductById(id: number): Observable<ProductVm> {
    return this.api.get<ProductoResponse>(`${API_ENDPOINTS.productos}/${id}`).pipe(map((item) => this.toVm(item)));
  }

  searchProducts(term: string, filter: ProductFilter): Observable<ProductVm[]> {
    return this.getProducts().pipe(map((items) => this.applyLocalSearch(items, term, filter)));
  }

  createProduct(request: ProductCreateRequest): Observable<ProductVm> {
    return this.api
      .post<ProductoResponse>(API_ENDPOINTS.productos, this.toApiRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  updateProduct(id: number, request: ProductUpdateRequest): Observable<ProductVm> {
    return this.api
      .put<ProductoResponse>(`${API_ENDPOINTS.productos}/${id}`, this.toApiRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  changeProductStatus(id: number, statusId: number): Observable<ProductVm> {
    return this.getProductById(id).pipe(
      map((product) => this.toUpdateRequest(product, statusId)),
      map((request) => this.toApiRequest(request)),
      switchMap((body) => this.api.put<ProductoResponse>(`${API_ENDPOINTS.productos}/${id}`, body)),
      map((item) => this.toVm(item)),
    );
  }

  deleteProduct(id: number): Observable<void> {
    return this.api.delete<void>(`${API_ENDPOINTS.productos}/${id}`);
  }

  getProductHistory(_id: number): Observable<never> {
    // TODO backend: implementar endpoint de historial de cambios y reemplazar esta excepcion.
    throw new Error('TODO: Endpoint de historial de productos no implementado en backend.');
  }

  applyLocalSearch(items: ProductVm[], term: string, filter: ProductFilter): ProductVm[] {
    const normalizedTerm = term.trim().toLowerCase();
    return items.filter((item) => {
      const searchable = [item.codigo, item.nombre, item.presentacion ?? '', item.descripcion ?? '', item.unidadMedida]
        .join(' ')
        .toLowerCase();

      if (normalizedTerm && !searchable.includes(normalizedTerm)) {
        return false;
      }

      if (filter.estado === 'ACTIVE' && !this.isActive(item)) {
        return false;
      }
      if (filter.estado === 'INACTIVE' && this.isActive(item)) {
        return false;
      }
      if (filter.presentacion && (item.presentacion ?? '') !== filter.presentacion) {
        return false;
      }
      if (filter.unidadMedida && item.unidadMedida !== filter.unidadMedida) {
        return false;
      }
      if (filter.stockBajo && item.stockDisponible > item.stockSeguridad) {
        return false;
      }
      if (filter.minPrecio !== null && item.precioBase < filter.minPrecio) {
        return false;
      }
      if (filter.maxPrecio !== null && item.precioBase > filter.maxPrecio) {
        return false;
      }
      return true;
    });
  }

  isActive(product: ProductVm): boolean {
    const label = this.getStatusLabel(product).toLowerCase();
    return product.estado?.id === 1 || label === 'activo' || label === 'habilitado';
  }

  getStatusLabel(product: ProductVm): string {
    const statusById = new Map([
      [1, 'Activo'],
      [2, 'Inactivo'],
      [3, 'Bloqueado'],
    ]);
    const normalizedName = (product.estado?.nombre ?? '').trim();
    return (statusById.get(product.estado?.id ?? 0) ?? normalizedName) || 'Sin estado';
  }

  private toVm(item: ProductoResponse): ProductVm {
    return {
      id: item.idProducto,
      codigo: `PRD-${item.idProducto.toString().padStart(4, '0')}`,
      nombre: item.nombreProducto,
      descripcion: item.descripcionTecnica,
      presentacion: this.resolvePresentacion(item.unidadMedida),
      unidadMedida: item.unidadMedida,
      precioBase: item.precioBaseUnitario,
      stockFisico: item.stockFisico,
      stockReservado: item.stockReservado,
      stockDisponible: item.stockDisponible,
      stockSeguridad: item.stockMinimoSeguridad,
      estado: this.normalizeStatus(item),
      concentracionUreaAus32: item.concentracionUreaAus32,
      fechaActualizacion: undefined,
      fechaCreacion: undefined,
    };
  }

  private toApiRequest(request: ProductCreateRequest | ProductUpdateRequest): ApiProductoRequest {
    const stockDisponible = Math.max(0, request.stockFisico - request.stockReservado);
    return {
      nombreProducto: request.nombre,
      descripcionTecnica: request.descripcion ?? '',
      unidadMedida: request.unidadMedida,
      precioBaseUnitario: request.precioBase,
      concentracionUreaAus32: request.concentracionUreaAus32,
      stockFisico: request.stockFisico,
      stockReservado: request.stockReservado,
      stockDisponible,
      stockMinimoSeguridad: request.stockSeguridad,
      idEstado: request.estadoId,
    };
  }

  private toUpdateRequest(product: ProductVm, statusId: number): ProductUpdateRequest {
    return {
      nombre: product.nombre,
      descripcion: product.descripcion,
      presentacion: product.presentacion,
      unidadMedida: product.unidadMedida,
      precioBase: product.precioBase,
      stockFisico: product.stockFisico,
      stockReservado: product.stockReservado,
      stockSeguridad: product.stockSeguridad,
      concentracionUreaAus32: product.concentracionUreaAus32 ?? 32.5,
      estadoId: statusId,
    };
  }

  private resolvePresentacion(unidadMedida: string): string {
    const normalized = unidadMedida.toUpperCase();
    if (normalized.includes('20')) {
      return '20L';
    }
    if (normalized.includes('IBC')) {
      return 'IBC';
    }
    if (normalized.includes('GRANEL')) {
      return 'Granel';
    }
    if (normalized.includes('CIL')) {
      return 'Cilindro';
    }
    return unidadMedida;
  }

  private normalizeStatus(item: ProductoResponse): ProductVm['estado'] {
    const rawEstado = (item as ProductoResponse & { estado?: LegacyEstadoShape }).estado;
    const id = rawEstado?.id ?? rawEstado?.idEstado ?? (item as ProductoResponse & { idEstado?: number }).idEstado;
    const rawName = rawEstado?.nombre ?? rawEstado?.descEstado;
    const statusById = new Map([
      [1, 'Activo'],
      [2, 'Inactivo'],
      [3, 'Bloqueado'],
    ]);

    if (id && statusById.has(id)) {
      return { id, nombre: statusById.get(id)! };
    }
    if (id || rawName) {
      return { id: id ?? 0, nombre: (rawName ?? 'Sin estado').trim() };
    }
    return undefined;
  }
}

interface ApiProductoRequest {
  nombreProducto: string;
  descripcionTecnica: string;
  unidadMedida: string;
  precioBaseUnitario: number;
  concentracionUreaAus32: number;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  stockMinimoSeguridad: number;
  idEstado: number;
}

interface LegacyEstadoShape {
  id?: number;
  nombre?: string;
  idEstado?: number;
  descEstado?: string;
}
