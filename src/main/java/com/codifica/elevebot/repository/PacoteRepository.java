package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PacoteRepository extends JpaRepository<Pacote, Integer> {

    @Query("SELECT p FROM Pacote p " +
            "WHERE p.cliente.id = :idCliente " +
            "AND p.dataExpiracao >= :dataAtual")
    Pacote findActivePacoteByCliente(@Param("idCliente") Integer idCliente,
                                     @Param("dataAtual") LocalDate dataAtual);

    boolean existsByClienteIdAndDataExpiracaoGreaterThanEqual(Integer clienteId, LocalDate dataExpiracao);
}