package com.example.demo.mapper;

import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.model.EstadoProducto;
import com.example.demo.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    private final ReferenceMapper referenceMapper;

    public ProductoMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public Producto toEntity(ProductoRequest request, EstadoProducto estadoProducto) {
        Producto producto = new Producto();
        producto.nombreProducto = request.nombreProducto();
        producto.descripcionTecnica = request.descripcionTecnica();
        producto.unidadMedida = request.unidadMedida();
        producto.precioBaseUnitario = request.precioBaseUnitario();
        producto.concentracionUreaAus32 = request.concentracionUreaAus32();
        producto.stockFisico = request.stockFisico();
        producto.stockReservado = request.stockReservado();
        producto.stockMinimoSeguridad = request.stockMinimoSeguridad();
        producto.estadoProducto = estadoProducto;
        return producto;
    }

    public void updateEntity(Producto producto, ProductoRequest request, EstadoProducto estadoProducto) {
        producto.nombreProducto = request.nombreProducto();
        producto.descripcionTecnica = request.descripcionTecnica();
        producto.unidadMedida = request.unidadMedida();
        producto.precioBaseUnitario = request.precioBaseUnitario();
        producto.concentracionUreaAus32 = request.concentracionUreaAus32();
        producto.stockFisico = request.stockFisico();
        producto.stockReservado = request.stockReservado();
        producto.stockMinimoSeguridad = request.stockMinimoSeguridad();
        producto.estadoProducto = estadoProducto;
    }

    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.idProducto,
                producto.nombreProducto,
                producto.descripcionTecnica,
                producto.unidadMedida,
                producto.precioBaseUnitario,
                producto.concentracionUreaAus32,
                producto.stockFisico,
                producto.stockReservado,
                stockDisponible(producto),
                producto.stockMinimoSeguridad,
                referenceMapper.toReference(producto.estadoProducto)
        );
    }

    private Integer stockDisponible(Producto producto) {
        if (producto.stockDisponible != null) {
            return producto.stockDisponible;
        }
        if (producto.stockFisico == null || producto.stockReservado == null) {
            return null;
        }
        return producto.stockFisico - producto.stockReservado;
    }
}
