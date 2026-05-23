package com.example.demo.exception;

import java.util.List;
import java.util.Map;

public class BusinessValidationException extends RuntimeException {
    private final Map<String, List<String>> fields;

    public BusinessValidationException(String message, Map<String, List<String>> fields) {
        super(message);
        this.fields = fields;
    }

    public Map<String, List<String>> getFields() {
        return fields;
    }
}
