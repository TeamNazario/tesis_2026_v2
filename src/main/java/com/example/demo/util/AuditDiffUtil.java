package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AuditDiffUtil {
    private static final String MASK = "***";
    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "password",
            "passwordHash",
            "token",
            "refreshToken",
            "secret",
            "apiKey"
    );
    private static final Set<String> TECHNICAL_FIELDS = Set.of(
            "hibernateLazyInitializer",
            "handler"
    );

    private final ObjectMapper objectMapper;

    public AuditDiffUtil() {
        this.objectMapper = new ObjectMapper();
    }

    public String convertirAJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(sanitized(object));
        } catch (JsonProcessingException ex) {
            return "{\"error\":\"No fue posible serializar auditoria\"}";
        }
    }

    public Map<String, Object> sanitized(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Map<?, ?> map) {
            return sanitizeMap(map);
        }
        if (isSimpleValue(object)) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("value", object);
            return value;
        }
        return sanitizeObject(object);
    }

    private Map<String, Object> sanitizeMap(Map<?, ?> map) {
        Map<String, Object> sanitized = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            if (TECHNICAL_FIELDS.contains(key)) {
                continue;
            }
            sanitized.put(key, sanitizeValue(key, entry.getValue()));
        }
        return sanitized;
    }

    private Map<String, Object> sanitizeObject(Object object) {
        Map<String, Object> sanitized = new LinkedHashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                sanitized.put(field.getName(), sanitizeValue(field.getName(), field.get(object)));
            } catch (IllegalAccessException ignored) {
                sanitized.put(field.getName(), null);
            }
        }
        return sanitized;
    }

    private Object sanitizeValue(String fieldName, Object value) {
        if (isSensitive(fieldName)) {
            return value == null ? null : MASK;
        }
        if (value instanceof Temporal temporal) {
            return temporal.toString();
        }
        if (value == null || isSimpleValue(value)) {
            return value;
        }
        if (value instanceof Collection<?> collection) {
            return collection.stream().map(this::compactRelatedValue).toList();
        }
        if (value instanceof Map<?, ?> map) {
            return sanitizeMap(map);
        }
        return compactRelatedValue(value);
    }

    private Object compactRelatedValue(Object value) {
        if (value == null || isSimpleValue(value)) {
            return value;
        }
        Map<String, Object> compact = new LinkedHashMap<>();
        for (Field field : value.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (field.getName().startsWith("id") || field.getName().toLowerCase().contains("nombre")
                    || field.getName().toLowerCase().contains("desc") || field.getName().toLowerCase().contains("correo")) {
                field.setAccessible(true);
                try {
                    compact.put(field.getName(), sanitizeValue(field.getName(), field.get(value)));
                } catch (IllegalAccessException ignored) {
                    compact.put(field.getName(), null);
                }
            }
        }
        return compact.isEmpty() ? String.valueOf(value) : compact;
    }

    private boolean isSensitive(String fieldName) {
        String normalized = fieldName == null ? "" : fieldName.trim();
        return SENSITIVE_FIELDS.stream().anyMatch(sensitive -> normalized.equalsIgnoreCase(sensitive)
                || normalized.toLowerCase().contains(sensitive.toLowerCase()));
    }

    private boolean isSimpleValue(Object value) {
        return value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Enum<?>;
    }
}
