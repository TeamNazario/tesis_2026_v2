import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { ProductoV1, ProductoV1CreateRequest, ProductoV1UpdateRequest } from '../models/v1.models';
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
    return this.api.get<ProductoV1[]>(API_ENDPOINTS.v1.productos).pipe(map((items) => items.map((item) => this.toVm(item))));
  }

  getProductsByEstado(estadoId: number): Observable<ProductVm[]> {
    return this.getProducts().pipe(map((items) => items.filter((item) => item.estado?.id === estadoId)));
  }

  getProductById(id: number): Observable<ProductVm> {
    return this.api.get<ProductoV1>(`${API_ENDPOINTS.v1.productos}/${id}`).pipe(map((item) => this.toVm(item)));
  }

  searchProducts(term: string, filter: ProductFilter): Observable<ProductVm[]> {
    return this.getProducts().pipe(map((items) => this.applyLocalSearch(items, term, filter)));
  }

  createProduct(request: ProductCreateRequest): Observable<ProductVm> {
    return this.api
      .post<ProductoV1>(API_ENDPOINTS.v1.productos, this.toCreateRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  updateProduct(id: number, request: ProductUpdateRequest): Observable<ProductVm> {
    return this.api
      .put<ProductoV1>(`${API_ENDPOINTS.v1.productos}/${id}`, this.toUpdateRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  changeProductStatus(id: number, idEstadoProducto: number): Observable<ProductVm> {
    return this.api
      .patch<ProductoV1>(`${API_ENDPOINTS.v1.productos}/${id}/estado`, { idEstadoProducto })
      .pipe(map((item) => this.toVm(item)));
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
      if (filter.estado === 'INACTIVE' && !this.isInactive(item)) {
        return false;
      }
      if (filter.estado === 'BLOCKED' && !this.isBlocked(item)) {
        return false;
      }
      if (filter.presentacion && (item.presentacion ?? '') !== filter.presentacion) {
        return false;
      }
      if (filter.unidadMedida && item.unidadMedida !== filter.unidadMedida) {
        return false;
      }
      if (filter.stockBajo && item.stockDisponible > item.stockMinimo) {
        return false;
      }
      return true;
    });
  }

  isActive(product: ProductVm): boolean {
    const id = this.getStatusId(product);
    if (id === 1) {
      return true;
    }
    if (id === 2 || id === 3) {
      return false;
    }
    const label = this.normalizeStatus(product.estado?.nombre);
    return label === 'activo' || label === 'habilitado';
  }

  isInactive(product: ProductVm): boolean {
    const id = this.getStatusId(product);
    if (id === 2) {
      return true;
    }
    const label = this.normalizeStatus(product.estado?.nombre);
    return label === 'inactivo' || label === 'inhabilitado';
  }

  isBlocked(product: ProductVm): boolean {
    const id = this.getStatusId(product);
    if (id === 3) {
      return true;
    }
    return this.normalizeStatus(product.estado?.nombre).includes('bloqueado');
  }

  getStatusLabel(product: ProductVm): string {
    const id = this.getStatusId(product);
    if (id === 1) {
      return 'Habilitado';
    }
    if (id === 2) {
      return 'Inhabilitado';
    }
    if (id === 3) {
      return 'Bloqueado';
    }
    return product.estado?.nombre?.trim() || 'Sin estado';
  }

  private toVm(item: ProductoV1): ProductVm {
    return {
      id: item.idProducto,
      codigo: `PRD-${item.idProducto.toString().padStart(4, '0')}`,
      nombre: item.nombreProducto,
      descripcion: undefined,
      presentacion: this.resolvePresentacion(item.unidadMedida),
      unidadMedida: item.unidadMedida,
      peso: item.peso ?? 0,
      volumen: item.volumen ?? 0,
      stockFisico: item.stockFisico,
      stockReservado: item.stockReservado,
      stockDisponible: item.stockDisponible,
      stockMinimo: item.stockMinimo,
      cantMinVenta: item.cantMinVenta,
      estado: item.idEstadoProducto || item.estadoProducto
        ? { id: item.idEstadoProducto ?? 0, nombre: item.estadoProducto?.trim() || this.getStatusNameById(item.idEstadoProducto) || 'Sin estado' }
        : undefined,
      fechaActualizacion: undefined,
      fechaCreacion: undefined,
    };
  }

  private toCreateRequest(request: ProductCreateRequest): ProductoV1CreateRequest {
    const stockDisponible = Math.max(0, request.stockFisico - request.stockReservado);
    return {
      nombreProducto: request.nombre,
      unidadMedida: request.unidadMedida,
      peso: request.peso,
      volumen: request.volumen,
      stockFisico: request.stockFisico,
      stockReservado: request.stockReservado,
      stockDisponible,
      stockMinimo: request.stockMinimo,
      cantMinVenta: request.cantMinVenta,
      idEstadoProducto: request.idEstadoProducto,
    };
  }

  private toUpdateRequest(request: ProductUpdateRequest): ProductoV1UpdateRequest {
    return this.toCreateRequest(request);
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

  private getStatusId(product: ProductVm): number | undefined {
    return product.estado?.id && product.estado.id > 0 ? product.estado.id : undefined;
  }

  private getStatusNameById(id?: number): string | undefined {
    if (id === 1) {
      return 'Habilitado';
    }
    if (id === 2) {
      return 'Inhabilitado';
    }
    if (id === 3) {
      return 'Bloqueado';
    }
    return undefined;
  }

  private normalizeStatus(value?: string): string {
    return (value ?? '')
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }
}
