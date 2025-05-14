package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    @Query("SELECT COUNT(a) > 0 FROM Agenda a WHERE a.pet.id = :petId")
    boolean existsByPetId(Integer petId);

}
