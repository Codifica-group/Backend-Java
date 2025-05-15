package com.codifica.elevebot.model;

import java.time.LocalDate;
import java.util.List;

public class Filtro {
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer clienteId;
    private Integer petId;
    private Integer racaId;
    private List<Integer> servicoId;

    public Filtro() {
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getRacaId() {
        return racaId;
    }

    public void setRacaId(Integer racaId) {
        this.racaId = racaId;
    }

    public List<Integer> getServicoId() {
        return servicoId;
    }

    public void setServicoId(List<Integer> servicoId) {
        this.servicoId = servicoId;
    }
}
