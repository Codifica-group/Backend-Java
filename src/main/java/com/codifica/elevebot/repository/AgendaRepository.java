package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

}
