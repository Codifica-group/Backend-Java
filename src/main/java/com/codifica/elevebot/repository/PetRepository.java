package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    Pet findByNome(String nome);

}
