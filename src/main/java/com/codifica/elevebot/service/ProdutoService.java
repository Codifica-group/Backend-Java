package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.ProdutoAdapter;
import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.model.Produto;
import com.codifica.elevebot.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    private ProdutoAdapter produtoAdapter;

    public String cadastrar(ProdutoDTO produtoDTO) {
        if(produtoRepository.existsByCategoriaProdutoAndNome(produtoDTO.getCategoria(), produtoDTO.getNome())) {
            throw new ConflictException("Não é possivel cadastrar dois produtos iguais.");
        }

        Produto produto = produtoAdapter.toEntity(produtoDTO);
        produtoRepository.save(produto);
        return "Produto cadastrado com sucesso";
    }

    public Map<String, List<ProdutoDTO>> listar() {
        List<Produto> produtos = produtoRepository.findAll();

        List<ProdutoDTO> listaDTO = produtos.stream()
                .map(ProdutoAdapter::toDTO)
                .collect(Collectors.toList());

        return listaDTO.stream().collect(Collectors.groupingBy(dto -> {
            switch (dto.getCategoria()) {
                case GASTOS_FIXOS:
                    return "Gastos Fixos";
                case MANUTENCAO:
                    return "Manutenção";
                case INSUMOS:
                    return "Insumos";
                case PRODUTO:
                    return "Produto";
                default:
                    return "Outros";
            }
        }));
    }
}
