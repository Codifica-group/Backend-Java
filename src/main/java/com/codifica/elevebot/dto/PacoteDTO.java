package com.codifica.elevebot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class PacoteDTO {

    private Integer id = 0;
    private Integer clienteId;
    private Integer pacoteId;
    private LocalDate dataInicio = LocalDate.now();
    private LocalDate dataExpiracao;
    private String status;

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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
