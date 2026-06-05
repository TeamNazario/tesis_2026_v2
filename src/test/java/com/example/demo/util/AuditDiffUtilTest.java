package com.example.demo.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AuditDiffUtilTest {
    private final AuditDiffUtil util = new AuditDiffUtil();

    @Test
    void convertirAJsonEnmascaraCamposSensibles() {
        String json = util.convertirAJson(Map.of(
                "correo", "usuario@demo.com",
                "passwordHash", "valor-secreto",
                "token", "jwt-secreto"
        ));

        assertThat(json).contains("\"correo\":\"usuario@demo.com\"");
        assertThat(json).contains("\"passwordHash\":\"***\"");
        assertThat(json).contains("\"token\":\"***\"");
        assertThat(json).doesNotContain("valor-secreto");
        assertThat(json).doesNotContain("jwt-secreto");
    }
}
