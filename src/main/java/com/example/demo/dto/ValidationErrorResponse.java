package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, List<String>> fields
) {
}
