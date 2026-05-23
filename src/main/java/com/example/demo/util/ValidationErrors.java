package com.example.demo.util;

import com.example.demo.exception.BusinessValidationException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ValidationErrors {
    private final Map<String, List<String>> fields = new LinkedHashMap<>();

    public void add(String field, String message) {
        fields.computeIfAbsent(field, ignored -> new ArrayList<>()).add(message);
    }

    public boolean hasErrors() {
        return !fields.isEmpty();
    }

    public void throwIfAny(String message) {
        if (hasErrors()) {
            throw new BusinessValidationException(message, fields);
        }
    }
}
