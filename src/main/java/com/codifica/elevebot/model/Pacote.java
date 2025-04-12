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

    private Integer idPacote;
    private LocalDate dataExpiracao;

    public Pacote() {}

    public Pacote(Integer id, Cliente cliente, Integer idPacote, LocalDate dataExpiracao) {
        this.id = id;
        this.cliente = cliente;
        this.idPacote = idPacote;
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
