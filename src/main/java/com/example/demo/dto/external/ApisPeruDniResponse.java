package com.example.demo.dto.external;

import tools.jackson.annotation.JsonAlias;

public record ApisPeruDniResponse(
        String dni,
        String nombres,
        @JsonAlias({"apellido_paterno", "apePaterno"})
        String apellidoPaterno,
        @JsonAlias({"apellido_materno", "apeMaterno"})
        String apellidoMaterno,
        @JsonAlias({"cod_verifica", "codigo_verificacion", "codigoVerificacion"})
        String codVerifica
) {
}
