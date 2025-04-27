package com.codifica.elevebot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class PacoteDTO {

    private Integer id = 0;
    private Integer clienteId;
    private Integer pacoteId;
    private LocalDate dataExpiracao = LocalDate.now();

    public PacoteDTO () {}

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getPacoteId() {
        return pacoteId;
    }

    public void setPacoteId(Integer pacoteId) {
        this.pacoteId = pacoteId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    @JsonProperty("dataInicio")
    public void setDataInicial(LocalDate dataInicio) {
        this.dataExpiracao = dataInicio;
    }
}
