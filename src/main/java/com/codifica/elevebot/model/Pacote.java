package com.codifica.elevebot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="cliente_pacote")
public class Pacote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cliente cliente;

    private Integer pacoteId;
    private LocalDate dataInicio;
    private LocalDate dataExpiracao;

    public Pacote() {}

    public Pacote(Cliente cliente, Integer pacoteId, LocalDate dataInicio, LocalDate dataExpiracao) {
        this.cliente = cliente;
        this.pacoteId = pacoteId;
        this.dataInicio = dataInicio;
        this.dataExpiracao = dataExpiracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getPacoteId() {
        return pacoteId;
    }

    public void setPacoteId(Integer pacoteId) {
        this.pacoteId = pacoteId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}
