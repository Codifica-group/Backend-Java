package com.codifica.elevebot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class PacoteDTO {

    private Integer id = 0;
    private Integer idCliente;
    private Integer idPacote;
    private LocalDate dataExpiracao = LocalDate.now();

    public PacoteDTO () {}

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdPacote() {
        return idPacote;
    }

    public void setIdPacote(Integer idPacote) {
        this.idPacote = idPacote;
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
