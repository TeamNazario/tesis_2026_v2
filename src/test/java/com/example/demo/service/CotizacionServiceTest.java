package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.CotizacionDetalleRequest;
import com.example.demo.dto.CotizacionRequest;
import com.example.demo.dto.CotizacionResponse;
import com.example.demo.mapper.CotizacionMapper;
import com.example.demo.mapper.ReferenceMapper;
import com.example.demo.model.Cliente;
import com.example.demo.model.Cotizacion;
import com.example.demo.model.CotizacionDetalle;
import com.example.demo.model.Producto;
import com.example.demo.model.ZonaDespacho;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.CotizacionDetalleRepository;
import com.example.demo.repository.CotizacionRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ZonaDespachoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CotizacionServiceTest {
    @Mock
    private CotizacionRepository cotizacionRepository;

    @Mock
    private CotizacionDetalleRepository detalleRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ZonaDespachoRepository zonaDespachoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    private CotizacionService service;

    @BeforeEach
    void setUp() {
        service = new CotizacionService(
                cotizacionRepository,
                detalleRepository,
                clienteRepository,
                zonaDespachoRepository,
                usuarioRepository,
                productoRepository,
                new CotizacionMapper(new ReferenceMapper()),
                BigDecimal.valueOf(0.18)
        );
    }

    @Test
    void createCalculatesSubtotalIgvAndTotalFromDetails() {
        Cliente cliente = new Cliente();
        cliente.idCliente = 1;
        cliente.razonSocial = "Cliente SAC";

        ZonaDespacho zona = new ZonaDespacho();
        zona.idZona = 1;
        zona.departamento = "Lima";
        zona.provincia = "Lima";

        Producto producto = new Producto();
        producto.idProducto = 1;
        producto.nombreProducto = "AUS32";

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(zonaDespachoRepository.findById(1)).thenReturn(Optional.of(zona));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(cotizacionRepository.save(any(Cotizacion.class))).thenAnswer(invocation -> {
            Cotizacion cotizacion = invocation.getArgument(0);
            cotizacion.idCotizacion = 50;
            return cotizacion;
        });
        when(detalleRepository.saveAll(any())).thenAnswer(invocation -> {
            List<CotizacionDetalle> detalles = invocation.getArgument(0);
            detalles.get(0).idDetalle = 100;
            return detalles;
        });

        CotizacionResponse response = service.create(new CotizacionRequest(
                1,
                1,
                null,
                LocalDateTime.of(2026, 5, 20, 10, 0),
                LocalDateTime.of(2026, 5, 27, 10, 0),
                "WEB",
                "EMITIDA",
                null,
                List.of(new CotizacionDetalleRequest(1, 2, BigDecimal.valueOf(100)))
        ));

        assertThat(response.subtotal()).isEqualByComparingTo("200.00");
        assertThat(response.igv()).isEqualByComparingTo("36.00");
        assertThat(response.montoTotal()).isEqualByComparingTo("236.00");
        assertThat(response.detalles()).hasSize(1);
    }
}
