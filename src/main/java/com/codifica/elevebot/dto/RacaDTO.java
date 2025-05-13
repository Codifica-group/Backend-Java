package com.codifica.elevebot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RacaDTO {

    private Integer id;
    private Integer porteId;
    private String nome;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String porteNome;

    public RacaDTO() {}

    public RacaDTO(Integer id, Integer porteId, String nome, String porteNome) {
        this.id = id;
        this.porteId = porteId;
        this.nome = nome;
        this.porteNome = porteNome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPorteId() {
        return porteId;
    }

    public void setPorteId(Integer porteId) {
        this.porteId = porteId;
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