package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    @Query("SELECT COUNT(a) > 0 FROM Agenda a WHERE a.pet.id = :petId")
    boolean existsByPetId(Integer petId);

    @Query("SELECT a FROM Agenda a WHERE NOT (a.dataHoraFim < :inicio OR a.dataHoraInicio > :fim)")
    List<Agenda> findConflitos(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT a FROM Agenda a WHERE a.id != :id AND NOT (a.dataHoraFim < :inicio OR a.dataHoraInicio > :fim)")
    List<Agenda> findConflitosExcluindoId(@Param("id") Integer id, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

}
