package com.codifica.elevebot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SugestaoDTO {

    private Double valor;
    private List<ServicoDTO> servico;
    private DeslocamentoDTO deslocamento;

    public SugestaoDTO(Double valor, List<ServicoDTO> servico, DeslocamentoDTO deslocamento) {
        this.valor = valor;
        this.servico = servico;
        this.deslocamento = deslocamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public List<ServicoDTO> getServico() {
        return servico;
    }

    public void setServico(List<ServicoDTO> servico) {
        this.servico = servico;
    }

    public DeslocamentoDTO getDeslocamento() {
        return deslocamento;
    }

    public void setDeslocamento(DeslocamentoDTO deslocamento) {
        this.deslocamento = deslocamento;
    }
}
