package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.ProdutoAdapter;
import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.model.Produto;
import com.codifica.elevebot.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    private ProdutoAdapter produtoAdapter;

    public String cadastrar(List<ProdutoDTO> produtosDTO) {
        List<Produto> produtos = produtosDTO.stream().map(produtoDTO -> {
            if(produtoRepository.existsByCategoriaProdutoAndNome(produtoDTO.getCategoria(), produtoDTO.getNome())) {
                throw new ConflictException("Não é possível cadastrar dois produtos iguais, remova: "
                        + produtoDTO.getNome());
            }
            return produtoAdapter.toEntity(produtoDTO);
        }).collect(Collectors.toList());

        produtoRepository.saveAll(produtos);
        return "Produtos cadastrados com sucesso.";
    }

    public Map<String, List<ProdutoDTO>> listar() {
        List<Produto> produtos = produtoRepository.findAll();

        List<ProdutoDTO> listaDTO = produtos.stream()
                .map(ProdutoAdapter::toDTO)
                .collect(Collectors.toList());

        Map<CategoriaProduto, List<ProdutoDTO>> produtosAgrupados = listaDTO.stream()
                .collect(Collectors.groupingBy(ProdutoDTO::getCategoria));

        Map<String, List<ProdutoDTO>> resultadoOrdenado = new LinkedHashMap<>();

        if (produtosAgrupados.containsKey(CategoriaProduto.GASTO_FIXO)) {
            resultadoOrdenado.put("Gasto Fixo", produtosAgrupados.get(CategoriaProduto.GASTO_FIXO));
        }
        if (produtosAgrupados.containsKey(CategoriaProduto.MANUTENCAO)) {
            resultadoOrdenado.put("Manutenção", produtosAgrupados.get(CategoriaProduto.MANUTENCAO));
        }
        if (produtosAgrupados.containsKey(CategoriaProduto.INSUMO)) {
            resultadoOrdenado.put("Insumo", produtosAgrupados.get(CategoriaProduto.INSUMO));
        }
        if (produtosAgrupados.containsKey(CategoriaProduto.PRODUTO)) {
            resultadoOrdenado.put("Produto", produtosAgrupados.get(CategoriaProduto.PRODUTO));
        }

        return resultadoOrdenado;
    }

    public String atualizar(Integer id, ProdutoDTO produtoDTO) {
        if (!produtoRepository.existsById(id)) {
            throw new NotFoundException("Produto não encontrado.");
        }

        Produto produto = produtoAdapter.toEntity(produtoDTO);
        produto.setId(id);
        produtoRepository.save(produto);
        return "Produto atualizado com sucesso.";
    }

    public String deletar(Integer id) {
        if (!produtoRepository.existsById(id)) {
            throw new NotFoundException("Produto não encontrado.");
        }

        produtoRepository.deleteById(id);
        return "Produto deletado com sucesso.";
    }
}
