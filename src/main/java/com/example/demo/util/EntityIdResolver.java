package com.example.demo.util;

import jakarta.persistence.Id;
import java.lang.reflect.Field;

public final class EntityIdResolver {
    private EntityIdResolver() {
    }

    public static <T, ID> void assignId(T entity, ID id) {
        Field idField = findIdField(entity.getClass());
        try {
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("No se pudo asignar el id de la entidad.", ex);
        }
    }

    private static Field findIdField(Class<?> entityClass) {
        Class<?> current = entityClass;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    return field;
                }
            }
            current = current.getSuperclass();
        }
        throw new IllegalStateException("La entidad no tiene un campo marcado con @Id.");
    }
}
