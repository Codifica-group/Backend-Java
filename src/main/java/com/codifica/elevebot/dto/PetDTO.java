package com.codifica.elevebot.dto;

public class PetDTO {

    private Integer racaId;
    private String nome;
    private Integer clienteId;

    public PetDTO() {}

    public PetDTO(Integer racaId, String nome, Integer clienteId) {
        this.racaId = racaId;
        this.nome = nome;
        this.clienteId = clienteId;
    }

    public Integer getRacaId() {
        return racaId;
    }
    public void setRacaId(Integer racaId) {
        this.racaId = racaId;
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