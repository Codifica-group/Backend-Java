package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Integer> {

    boolean existsByNome(String nome);

}