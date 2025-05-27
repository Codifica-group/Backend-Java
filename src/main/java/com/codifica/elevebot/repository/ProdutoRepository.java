package com.codifica.elevebot.repository;

import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    public boolean existsByCategoriaProdutoAndNome(CategoriaProduto categoriaProduto, String nome);

    public boolean existsByCategoriaProduto(CategoriaProduto categoriaProduto);
}
