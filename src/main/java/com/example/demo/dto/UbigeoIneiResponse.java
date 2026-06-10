package com.example.demo.dto;

public class UbigeoIneiResponse {

    private Integer idUbigeoInei;
    private String ubigeo;
    private String distrito;
    private String provincia;
    private String departamento;
    private Boolean cobertura;

    public UbigeoIneiResponse() {}

    public UbigeoIneiResponse(Integer idUbigeoInei, String ubigeo, String distrito,
                               String provincia, String departamento, Boolean cobertura) {
        this.idUbigeoInei = idUbigeoInei;
        this.ubigeo = ubigeo;
        this.distrito = distrito;
        this.provincia = provincia;
        this.departamento = departamento;
        this.cobertura = cobertura;
    }

    public Integer getIdUbigeoInei() { return idUbigeoInei; }
    public void setIdUbigeoInei(Integer idUbigeoInei) { this.idUbigeoInei = idUbigeoInei; }

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
}
