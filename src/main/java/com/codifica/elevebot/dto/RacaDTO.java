package com.codifica.elevebot.dto;

import com.codifica.elevebot.model.Porte;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RacaDTO {

    private Integer id;
    private Integer porte_id;
    private String nome;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String porteNome;

    public RacaDTO() {}

    public RacaDTO(Integer id, Integer porte_id, String nome, String porteNome) {
        this.id = id;
        this.porte_id = porte_id;
        this.nome = nome;
        this.porteNome = porteNome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPorte_id() {
        return porte_id;
    }

    public void setPorte_id(Integer porte_id) {
        this.porte_id = porte_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPorteNome() {
        return porteNome;
    }

    public void setPorteNome(String porteNome) {
        this.porteNome = porteNome;
    }
}