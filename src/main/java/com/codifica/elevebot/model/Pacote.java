package com.codifica.elevebot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;

import java.time.LocalDate;

@Entity
@Table(name="cliente_pacote")
public class Pacote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idCliente;
    private Integer idPacote;

    @Future
    private LocalDate dataExpiracao;

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

    public @Future LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(@Future LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}
