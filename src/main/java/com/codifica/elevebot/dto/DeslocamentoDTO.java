package com.codifica.elevebot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeslocamentoDTO {

    private Double tempo;
    private Double distanciaKm;
    private Double valor;

    public DeslocamentoDTO(Double tempo, Double distanciaKm, Double valor) {
        this.tempo = tempo;
        this.distanciaKm = distanciaKm;
        this.valor = valor;
    }

    public Double getTempo() {
        return tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
