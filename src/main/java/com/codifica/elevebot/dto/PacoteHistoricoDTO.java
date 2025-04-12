package com.codifica.elevebot.dto;

import java.time.LocalDate;

public class PacoteHistoricoDTO {

    private Integer id;
    private Integer idCliente;
    private Integer idPacote;
    private LocalDate dataExpiracao;

    public PacoteHistoricoDTO() {
    }

    public PacoteHistoricoDTO(Integer id, Integer idCliente, Integer idPacote, LocalDate dataExpiracao) {
        this.id = id;
        this.idCliente = idCliente;
        this.idPacote = idPacote;
        this.dataExpiracao = dataExpiracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}