package com.example.demo.util;

import com.example.demo.dto.ReporteFiltroRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class FileNameUtil {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    public String reporte(String baseName, ReporteFiltroRequest filter) {
        String inicio = filter.fechaInicio() == null ? null : filter.fechaInicio().format(FORMAT);
        String fin = filter.fechaFin() == null ? null : filter.fechaFin().format(FORMAT);
        String suffix;
        if (inicio != null && fin != null) {
            suffix = inicio + "_" + fin;
        } else {
            suffix = LocalDate.now().format(FORMAT);
        }
        return baseName + "_" + suffix + ".xlsx";
    }
}
