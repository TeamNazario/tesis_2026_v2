package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.mapper.ProductoMapper;
import com.example.demo.mapper.ReferenceMapper;
import com.example.demo.model.EstadoProducto;
import com.example.demo.model.Producto;
import com.example.demo.repository.EstadoProductoRepository;
import com.example.demo.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private EstadoProductoRepository estadoProductoRepository;

    private ProductoService service;

    @BeforeEach
    void setUp() {
        service = new ProductoService(
                productoRepository,
                estadoProductoRepository,
                new ProductoMapper(new ReferenceMapper())
        );
    }

    @Test
    void createCalculatesResponseAndPersistsProduct() {
        EstadoProducto estadoProducto = new EstadoProducto();
        estadoProducto.idEstadoProducto = 1;
        estadoProducto.descEstadoProducto = "Habilitado";

        when(estadoProductoRepository.findById(1)).thenReturn(Optional.of(estadoProducto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto producto = invocation.getArgument(0);
            producto.idProducto = 10;
            return producto;
        });

        ProductoResponse response = service.create(validRequest());

        assertThat(response.idProducto()).isEqualTo(10);
        assertThat(response.estado().nombre()).isEqualTo("Habilitado");
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void createRejectsReservedStockGreaterThanPhysicalStock() {
        ProductoRequest request = new ProductoRequest(
                "AUS32",
                "Descripcion",
                "GL",
                BigDecimal.TEN,
                BigDecimal.valueOf(32.5),
                10,
                100,
                0,
                20,
                1
        );

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessage("El producto contiene datos inconsistentes.")
                .satisfies(ex -> assertThat(((BusinessValidationException) ex).getFields())
                        .containsKey("stockReservado"));
    }

    private ProductoRequest validRequest() {
        return new ProductoRequest(
                "AUS32",
                "Descripcion",
                "GL",
                BigDecimal.TEN,
                BigDecimal.valueOf(32.5),
                100,
                10,
                90,
                20,
                1
        );
    }
}
