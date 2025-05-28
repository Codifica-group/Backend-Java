package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Agenda;
import com.codifica.elevebot.model.AgendaServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaServicoRepository extends JpaRepository<AgendaServico, Integer> {

    List<AgendaServico> findByAgenda(Agenda agenda);

    boolean existsByServicoId(Integer servicoId);

}
