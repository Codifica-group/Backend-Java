package com.codifica.elevebot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Total {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dataInicio;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dataFim;

    private Double Total;
    private Double Entrada;
    private Double Saida;

    public Total () {}

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

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public Double getEntrada() {
        return Entrada;
    }

    public void setEntrada(Double entrada) {
        Entrada = entrada;
    }

    public Double getSaida() {
        return Saida;
    }

    public void setSaida(Double saida) {
        Saida = saida;
    }
}
