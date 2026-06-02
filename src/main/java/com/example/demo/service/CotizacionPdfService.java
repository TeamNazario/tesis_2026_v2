package com.example.demo.service;

import com.example.demo.dto.CotizacionPdfResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ContactoCliente;
import com.example.demo.model.Cotizacion;
import com.example.demo.model.CotizacionDetalle;
import com.example.demo.repository.ContactoClienteRepository;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CotizacionPdfService {
    private static final Color BIO_GREEN = new Color(0, 112, 0);
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final CotizacionV1Service cotizacionService;
    private final ContactoClienteRepository contactoRepository;
    private final Path storageDir;

    public CotizacionPdfService(
            CotizacionV1Service cotizacionService,
            ContactoClienteRepository contactoRepository,
            @Value("${app.files.cotizaciones-dir:storage/cotizaciones}") String storageDir
    ) {
        this.cotizacionService = cotizacionService;
        this.contactoRepository = contactoRepository;
        this.storageDir = Path.of(storageDir);
    }

    @Transactional
    public CotizacionPdfResponse generarPdfCotizacion(Integer idCotizacion) {
        Cotizacion cotizacion = cotizacionService.findEntity(idCotizacion);
        String fileName = construirNombreArchivo(cotizacion);
        Path file = storageDir.resolve(fileName).normalize();
        try {
            Files.createDirectories(storageDir);
            try (OutputStream output = Files.newOutputStream(file)) {
                writePdf(cotizacion, output);
            }
        } catch (IOException | DocumentException ex) {
            throw new IllegalStateException("No fue posible generar el PDF de la cotizacion.");
        }
        String relativePath = "cotizaciones/" + fileName;
        cotizacionService.updatePdfPath(idCotizacion, relativePath);
        return new CotizacionPdfResponse(idCotizacion, relativePath, fileName, "PDF generado correctamente.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Resource> descargarPdfCotizacion(Integer idCotizacion) {
        Cotizacion cotizacion = cotizacionService.findEntity(idCotizacion);
        if (cotizacion.pdfPath == null || cotizacion.pdfPath.isBlank()) {
            throw new ResourceNotFoundException("PDF de cotizacion", idCotizacion);
        }
        Path file = resolvePdfPath(cotizacion.pdfPath);
        if (!Files.exists(file)) {
            throw new ResourceNotFoundException("PDF de cotizacion", idCotizacion);
        }
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(file.getFileName().toString()).build().toString())
                .body(resource);
    }

    public String construirNombreArchivo(Cotizacion cotizacion) {
        String fecha = cotizacion.fechaEmision == null ? FILE_DATE.format(java.time.LocalDateTime.now()) : cotizacion.fechaEmision.format(FILE_DATE);
        return "COTIZACION-C001-" + String.format("%06d", cotizacion.idCotizacion) + "-" + fecha + ".pdf";
    }

    private void writePdf(Cotizacion cotizacion, OutputStream output) throws DocumentException {
        Document document = new Document(PageSize.A4, 28, 28, 24, 24);
        PdfWriter.getInstance(document, output);
        document.open();
        document.add(header(cotizacion));
        document.add(Chunk.NEWLINE);
        document.add(clientTable(cotizacion));
        document.add(Chunk.NEWLINE);
        document.add(itemsTable(cotizacion));
        document.add(Chunk.NEWLINE);
        document.add(totalsTable(cotizacion));
        document.add(Chunk.NEWLINE);
        document.add(section("OBSERVACIONES", cotizacion.observaciones == null || cotizacion.observaciones.isBlank() ? "-" : cotizacion.observaciones));
        document.add(Chunk.NEWLINE);
        document.add(section("CONDICIONES COMERCIALES COTIZACION", "PLAZO DE VALIDEZ DE COTIZACION: La cotizacion tiene un plazo de validez de 15 dias."));
        document.add(Chunk.NEWLINE);
        document.add(bankTable());
        document.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph("GRACIAS POR SU PREFERENCIA...", font(10, Font.BOLD, BIO_GREEN));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
        document.close();
    }

    private PdfPTable header(Cotizacion cotizacion) {
        PdfPTable table = new PdfPTable(new float[] {1.2f, 3.8f, 1.6f});
        table.setWidthPercentage(100);
        table.addCell(cell("BIOFLUID\nD32", font(18, Font.BOLD, BIO_GREEN), Element.ALIGN_CENTER, true));

        Paragraph company = new Paragraph();
        company.add(new Phrase("BIOFLUID D32 E.I.R.L.\n", font(12, Font.BOLD, Color.BLACK)));
        company.add(new Phrase("Correo: ventas@biofluidd-32.com\n", font(8, Font.NORMAL, Color.BLACK)));
        company.add(new Phrase("Pagina web: https://biofluidd-32.com\n", font(8, Font.NORMAL, Color.BLACK)));
        company.add(new Phrase("Direccion: Mza. B Lote. 1 Urb. Altos del Valle - Moche - Trujillo - La Libertad", font(8, Font.NORMAL, Color.BLACK)));
        table.addCell(cell(company, Element.ALIGN_CENTER, true));

        table.addCell(cell("RUC: 20613142739\n\nCOTIZACION\n" + cotizacionNumber(cotizacion), font(9, Font.BOLD, Color.BLACK), Element.ALIGN_CENTER, true));
        return table;
    }

    private PdfPTable clientTable(Cotizacion cotizacion) {
        ContactoCliente contacto = contactoRepository.findByClienteIdCliente(cotizacion.cliente.idCliente).stream().findFirst().orElse(null);
        PdfPTable table = new PdfPTable(new float[] {1.2f, 3f, 1.1f, 2f});
        table.setWidthPercentage(100);
        table.addCell(headerCell("CLIENTE"));
        table.addCell(value(cotizacion.cliente.razonSocial));
        table.addCell(headerCell("FECHA"));
        table.addCell(value(cotizacion.fechaEmision == null ? "-" : cotizacion.fechaEmision.format(DATE)));
        table.addCell(headerCell("RUC"));
        table.addCell(value(cotizacion.cliente.ruc));
        table.addCell(headerCell("MONEDA"));
        table.addCell(value(cotizacion.moneda));
        table.addCell(headerCell("DIRECCION"));
        table.addCell(value(cotizacion.cliente.direccion));
        table.addCell(headerCell("VENDEDOR"));
        table.addCell(value(fullName(cotizacion.vendedor)));
        table.addCell(headerCell("CONTACTO"));
        table.addCell(value(contacto == null ? "-" : fullName(contacto)));
        table.addCell(headerCell("CELULAR"));
        table.addCell(value(contacto == null ? "-" : contacto.celular));
        return table;
    }

    private PdfPTable itemsTable(Cotizacion cotizacion) {
        PdfPTable table = new PdfPTable(new float[] {0.5f, 0.8f, 0.9f, 4f, 1f, 1f});
        table.setWidthPercentage(100);
        for (String title : List.of("ITEM", "CANT.", "U.M.", "DESCRIPCION", "P.U.", "IMPORTE")) {
            table.addCell(headerCell(title));
        }
        int index = 1;
        for (CotizacionDetalle detalle : cotizacion.detalles) {
            BigDecimal importe = money(detalle.precioUni.multiply(BigDecimal.valueOf(detalle.cantidad)));
            table.addCell(value(String.valueOf(index++), Element.ALIGN_CENTER));
            table.addCell(value(String.valueOf(detalle.cantidad), Element.ALIGN_CENTER));
            table.addCell(value(detalle.producto.unidadMedida, Element.ALIGN_CENTER));
            table.addCell(value(detalle.producto.nombreProducto));
            table.addCell(value(formatMoney(detalle.precioUni), Element.ALIGN_RIGHT));
            table.addCell(value(formatMoney(importe), Element.ALIGN_RIGHT));
        }
        return table;
    }

    private PdfPTable totalsTable(Cotizacion cotizacion) {
        PdfPTable table = new PdfPTable(new float[] {4f, 1.2f, 1f});
        table.setWidthPercentage(100);
        table.addCell(cell("SON: " + amountInWords(cotizacion.importeTotal, cotizacion.moneda), font(8, Font.BOLD, Color.BLACK), Element.ALIGN_LEFT, true));
        table.addCell(headerCell("OP. GRAVADA"));
        table.addCell(value(formatMoney(cotizacion.subtotal), Element.ALIGN_RIGHT));
        table.addCell(borderless(""));
        table.addCell(headerCell("I.G.V"));
        table.addCell(value(formatMoney(cotizacion.igv), Element.ALIGN_RIGHT));
        table.addCell(borderless(""));
        table.addCell(headerCell("IMPORTE TOTAL"));
        table.addCell(value(formatMoney(cotizacion.importeTotal), Element.ALIGN_RIGHT));
        return table;
    }

    private PdfPTable bankTable() {
        PdfPTable table = new PdfPTable(new float[] {1.2f, 1f, 2f, 2.5f});
        table.setWidthPercentage(100);
        for (String title : List.of("BANCO", "MONEDA", "NRO. CUENTA", "NRO. CUENTA CCI")) {
            table.addCell(headerCell(title));
        }
        addBank(table, "INTERBANK", "SOLES", "6003007005005", "00360000300700500546");
        addBank(table, "BBVA", "SOLES", "0011 0146 0200484260", "011 146 000200484260 81");
        addBank(table, "BCP", "SOLES", "19105583763063", "00219110558376306354");
        return table;
    }

    private Paragraph section(String title, String body) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(title + "\n", font(9, Font.BOLD, BIO_GREEN)));
        paragraph.add(new Phrase(body, font(8, Font.NORMAL, Color.BLACK)));
        return paragraph;
    }

    private void addBank(PdfPTable table, String bank, String moneda, String cuenta, String cci) {
        table.addCell(value(bank));
        table.addCell(value(moneda));
        table.addCell(value(cuenta));
        table.addCell(value(cci));
    }

    private PdfPCell headerCell(String text) {
        PdfPCell cell = cell(text, font(8, Font.BOLD, Color.WHITE), Element.ALIGN_CENTER, true);
        cell.setBackgroundColor(BIO_GREEN);
        return cell;
    }

    private PdfPCell value(String text) {
        return value(text, Element.ALIGN_LEFT);
    }

    private PdfPCell value(String text, int alignment) {
        return cell(text == null || text.isBlank() ? "-" : text, font(8, Font.NORMAL, Color.BLACK), alignment, true);
    }

    private PdfPCell borderless(String text) {
        PdfPCell cell = cell(text, font(8, Font.NORMAL, Color.BLACK), Element.ALIGN_LEFT, false);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell cell(String text, Font font, int alignment, boolean border) {
        return cell(new Phrase(text, font), alignment, border);
    }

    private PdfPCell cell(Phrase phrase, int alignment, boolean border) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        if (!border) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }

    private PdfPCell cell(Paragraph paragraph, int alignment, boolean border) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        if (!border) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }

    private Font font(int size, int style, Color color) {
        return new Font(Font.HELVETICA, size, style, color);
    }

    private Path resolvePdfPath(String pdfPath) {
        String fileName = pdfPath.replace("\\", "/");
        int slash = fileName.lastIndexOf('/');
        if (slash >= 0) {
            fileName = fileName.substring(slash + 1);
        }
        return storageDir.resolve(fileName).normalize();
    }

    private String cotizacionNumber(Cotizacion cotizacion) {
        return "C001-" + String.format("%06d", cotizacion.idCotizacion);
    }

    private String fullName(com.example.demo.model.Usuario usuario) {
        if (usuario == null) {
            return "-";
        }
        return String.join(" ", safe(usuario.nombres), safe(usuario.apellidoPaterno), safe(usuario.apellidoMaterno)).trim();
    }

    private String fullName(ContactoCliente contacto) {
        return String.join(" ", safe(contacto.nombre), safe(contacto.apellidoPaterno), safe(contacto.apellidoMaterno)).trim();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String formatMoney(BigDecimal value) {
        return money(value).toPlainString();
    }

    private BigDecimal money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }

    private String amountInWords(BigDecimal amount, String moneda) {
        BigDecimal safeAmount = money(amount);
        return safeAmount.toPlainString() + " " + (moneda == null ? "" : moneda);
    }
}
