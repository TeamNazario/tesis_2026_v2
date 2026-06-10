package com.example.demo.dto;

public class UbigeoCoberturaResponse {

    private String ubigeo;
    private String distrito;
    private String provincia;
    private String departamento;
    private Boolean cobertura;
    private String mensaje;

    public UbigeoCoberturaResponse() {}

    public UbigeoCoberturaResponse(String ubigeo, String distrito, String provincia,
                                    String departamento, Boolean cobertura) {
        this.ubigeo = ubigeo;
        this.distrito = distrito;
        this.provincia = provincia;
        this.departamento = departamento;
        this.cobertura = cobertura;
        this.mensaje = cobertura
                ? "BIOFLUID D32 tiene cobertura de despacho en esta ubicacion."
                : "BIOFLUID D32 no tiene cobertura de despacho en esta ubicacion actualmente.";
    }

    public String getUbigeo() { return ubigeo; }
    public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public Boolean getCobertura() { return cobertura; }
    public void setCobertura(Boolean cobertura) { this.cobertura = cobertura; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
