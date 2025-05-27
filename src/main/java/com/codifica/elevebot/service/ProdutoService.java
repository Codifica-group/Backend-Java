package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.ProdutoAdapter;
import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.model.Produto;
import com.codifica.elevebot.repository.CategoriaProdutoRepository;
import com.codifica.elevebot.repository.DespesaRepository;
import com.codifica.elevebot.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private ProdutoAdapter produtoAdapter;

    public Object cadastrar(List<ProdutoDTO> produtosDTO) {
        List<Produto> produtos = produtosDTO.stream().map(produtoDTO -> {
            CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(produtoDTO.getCategoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada para ID: " + produtoDTO.getCategoriaId()));

            if (produtoRepository.existsByCategoriaProdutoAndNome(categoriaProduto, produtoDTO.getNome())) {
                throw new ConflictException("Não é possível cadastrar dois produtos iguais na mesma categoria.");
            }

            Produto produto = produtoAdapter.toEntity(produtoDTO);
            produto.setCategoriaProduto(categoriaProduto);
            return produto;
        }).collect(Collectors.toList());

        Map<String, Object> resposta = new HashMap<>();
        if (produtos.size() == 1) {
            Produto produto = produtoRepository.save(produtos.get(0));
            resposta.put("mensagem", "Produto cadastrado com sucesso.");
            resposta.put("id", produto.getId());
            return resposta;
        }

        produtoRepository.saveAll(produtos);
        resposta.put("mensagem", "Produtos cadastrados com sucesso.");
        return resposta;
    }

    public Map<String, List<ProdutoDTO>> listar() {
        List<CategoriaProduto> categorias = categoriaProdutoRepository.findAll();

        List<ProdutoDTO> listaDTO = produtoRepository.findAll().stream()
                .map(produtoAdapter::toDTO)
                .collect(Collectors.toList());

        Map<String, List<ProdutoDTO>> produtosAgrupados = new LinkedHashMap<>();
        for (CategoriaProduto categoria : categorias) {
            List<ProdutoDTO> produtosDaCategoria = listaDTO.stream()
                    .filter(produtoDTO -> produtoDTO.getCategoriaId().equals(categoria.getId()))
                    .collect(Collectors.toList());
            if (!produtosDaCategoria.isEmpty()) {
                produtosAgrupados.put(categoria.getNome(), produtosDaCategoria);
            }
        }

        return produtosAgrupados;
    }

    public Object atualizar(Integer id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));

        if (despesaRepository.existsByProduto_Id(id)) {
            throw new ConflictException("Não é possível atualizar produtos que possuem despesas cadastradas.");
        }

        CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(produtoDTO.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada para ID: " + produtoDTO.getCategoriaId()));

        Produto produtoAtualizado = produtoAdapter.toEntity(produtoDTO);
        produtoAtualizado.setId(id);
        produtoAtualizado.setCategoriaProduto(categoriaProduto);
        produtoRepository.save(produtoAtualizado);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Produto atualizado com sucesso.");
        resposta.put("id", produtoAtualizado.getId());
        return resposta;
    }

    public String deletar(Integer id) {
        if (!produtoRepository.existsById(id)) {
            throw new NotFoundException("Produto não encontrado.");
        }

        if (despesaRepository.existsByProduto_Id(id)) {
            throw new ConflictException("Não é possível deletar produtos que possuem despesas cadastradas.");
        }

        produtoRepository.deleteById(id);
        return "Produto deletado com sucesso.";
    }
}