package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ubigeo_inei")
public class UbigeoInei {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubigeo_inei")
    private Integer idUbigeoInei;

    @Column(name = "ubigeo", nullable = false, length = 6)
    private String ubigeo;

    @Column(name = "distrito", nullable = false, length = 150)
    private String distrito;

    @Column(name = "provincia", nullable = false, length = 150)
    private String provincia;

    @Column(name = "departamento", nullable = false, length = 150)
    private String departamento;

    @Column(name = "flag_cobertura", nullable = false)
    private Integer flagCobertura;

    public UbigeoInei() {}

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

    public Integer getFlagCobertura() { return flagCobertura; }
    public void setFlagCobertura(Integer flagCobertura) { this.flagCobertura = flagCobertura; }
}
