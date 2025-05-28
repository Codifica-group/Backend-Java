package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer> {

    public boolean existsByNome(String nome);

}
