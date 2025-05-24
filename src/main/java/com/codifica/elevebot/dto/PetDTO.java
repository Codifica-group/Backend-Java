package com.codifica.elevebot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetDTO {

    private Integer id;
    private Integer racaId;
    private String nome;
    private Integer clienteId;
    private RacaDTO raca;

    public PetDTO() {}

    public PetDTO(Integer racaId, String nome, Integer clienteId) {
        this.racaId = racaId;
        this.nome = nome;
        this.clienteId = clienteId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRacaId() {
        return racaId;
    }
    public void setRacaId(Integer racaId) {
        this.racaId = racaId;
    }

    public RacaDTO getRaca() {
        return raca;
    }

    public void setRaca(RacaDTO raca) {
        this.raca = raca;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Integer getClienteId() {
        return clienteId;
    }
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}