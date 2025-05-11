package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Raca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacaRepository extends JpaRepository<Raca, Integer> {

    public boolean existsByNome(String nome);

}
