package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByNome(String nome);

}
