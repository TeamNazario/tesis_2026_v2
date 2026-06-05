package com.example.demo.util;

import com.example.demo.dto.ReporteFiltroRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelExportUtil {
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public <T> byte[] buildWorkbook(
            String title,
            String sheetName,
            String generatedBy,
            ReporteFiltroRequest filter,
            List<Column<T>> columns,
            List<T> rows
    ) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(safeSheetName(sheetName));
            Styles styles = styles(workbook);

            int rowIndex = 0;
            Row titleRow = sheet.createRow(rowIndex++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(styles.title);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, Math.max(0, columns.size() - 1)));

            rowIndex = writeMeta(sheet, styles, rowIndex, generatedBy, filter);
            rowIndex++;

            Row header = sheet.createRow(rowIndex++);
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns.get(i).header());
                cell.setCellStyle(styles.header);
            }

            if (rows.isEmpty()) {
                Row empty = sheet.createRow(rowIndex++);
                Cell cell = empty.createCell(0);
                cell.setCellValue("No se encontraron registros para los filtros aplicados.");
                cell.setCellStyle(styles.text);
                sheet.addMergedRegion(new CellRangeAddress(empty.getRowNum(), empty.getRowNum(), 0, Math.max(0, columns.size() - 1)));
            } else {
                for (T item : rows) {
                    Row row = sheet.createRow(rowIndex++);
                    for (int i = 0; i < columns.size(); i++) {
                        Object value = columns.get(i).value().apply(item);
                        writeCell(row.createCell(i), value, styles);
                    }
                }
            }

            int headerRowIndex = rowIndex - rows.size() - (rows.isEmpty() ? 1 : 0) - 1;
            sheet.createFreezePane(0, headerRowIndex + 1);
            sheet.setAutoFilter(new CellRangeAddress(headerRowIndex, Math.max(headerRowIndex, rowIndex - 1), 0, Math.max(0, columns.size() - 1)));
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, Math.min(Math.max(sheet.getColumnWidth(i), 3600), 14000));
            }

            workbook.write(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("No fue posible generar el archivo Excel.", ex);
        }
    }

    private int writeMeta(Sheet sheet, Styles styles, int rowIndex, String generatedBy, ReporteFiltroRequest filter) {
        rowIndex = meta(sheet, styles, rowIndex, "Empresa", "BIOFLUID D32 E.I.R.L.");
        rowIndex = meta(sheet, styles, rowIndex, "Fecha generacion", LocalDateTime.now().format(DATE_TIME));
        rowIndex = meta(sheet, styles, rowIndex, "Usuario", generatedBy == null || generatedBy.isBlank() ? "SISTEMA" : generatedBy);
        rowIndex = meta(sheet, styles, rowIndex, "Filtros", filters(filter));
        return rowIndex;
    }

    private int meta(Sheet sheet, Styles styles, int rowIndex, String label, String value) {
        Row row = sheet.createRow(rowIndex++);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(styles.metaLabel);
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(styles.text);
        return rowIndex;
    }

    private void writeCell(Cell cell, Object value, Styles styles) {
        if (value == null) {
            cell.setCellValue("");
            cell.setCellStyle(styles.text);
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
            cell.setCellStyle(value instanceof BigDecimal ? styles.money : styles.number);
        } else if (value instanceof LocalDateTime dateTime) {
            cell.setCellValue(dateTime.format(DATE_TIME));
            cell.setCellStyle(styles.text);
        } else if (value instanceof LocalDate date) {
            cell.setCellValue(date.format(DATE));
            cell.setCellStyle(styles.text);
        } else {
            cell.setCellValue(String.valueOf(value));
            cell.setCellStyle(styles.text);
        }
    }

    private Styles styles(Workbook workbook) {
        DataFormat dataFormat = workbook.createDataFormat();

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        CellStyle title = workbook.createCellStyle();
        title.setFont(titleFont);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        CellStyle header = workbook.createCellStyle();
        header.setFont(headerFont);
        header.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header.setAlignment(HorizontalAlignment.CENTER);
        header.setBorderBottom(BorderStyle.THIN);

        Font metaFont = workbook.createFont();
        metaFont.setBold(true);
        CellStyle metaLabel = workbook.createCellStyle();
        metaLabel.setFont(metaFont);

        CellStyle text = workbook.createCellStyle();
        text.setWrapText(false);

        CellStyle number = workbook.createCellStyle();
        number.setDataFormat(dataFormat.getFormat("#,##0"));

        CellStyle money = workbook.createCellStyle();
        money.setDataFormat(dataFormat.getFormat("#,##0.00"));

        return new Styles(title, header, metaLabel, text, number, money);
    }

    private String filters(ReporteFiltroRequest filter) {
        StringBuilder builder = new StringBuilder();
        append(builder, "fechaInicio", filter.fechaInicio());
        append(builder, "fechaFin", filter.fechaFin());
        append(builder, "idEstadoCotizacion", filter.idEstadoCotizacion());
        append(builder, "idCliente", filter.idCliente());
        append(builder, "idVendedor", filter.idVendedor());
        append(builder, "idProducto", filter.idProducto());
        append(builder, "idTipoCliente", filter.idTipoCliente());
        append(builder, "idEstadoProducto", filter.idEstadoProducto());
        append(builder, "moneda", filter.moneda());
        append(builder, "departamento", filter.departamento());
        append(builder, "provincia", filter.provincia());
        append(builder, "distrito", filter.distrito());
        append(builder, "stockBajo", filter.stockBajo());
        append(builder, "search", filter.search());
        return builder.isEmpty() ? "Sin filtros" : builder.toString();
    }

    private void append(StringBuilder builder, String label, Object value) {
        if (value == null || (value instanceof String text && text.isBlank())) {
            return;
        }
        if (!builder.isEmpty()) {
            builder.append(" | ");
        }
        builder.append(label).append(": ").append(value);
    }

    private String safeSheetName(String sheetName) {
        return sheetName.replaceAll("[\\\\/?*\\[\\]:]", " ").trim();
    }

    public record Column<T>(String header, Function<T, Object> value) {}

    private record Styles(CellStyle title, CellStyle header, CellStyle metaLabel, CellStyle text, CellStyle number, CellStyle money) {}
}
