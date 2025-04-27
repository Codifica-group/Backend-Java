package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.DespesaAdapter;
import com.codifica.elevebot.dto.DespesaDTO;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Despesa;
import com.codifica.elevebot.model.Produto;
import com.codifica.elevebot.repository.DespesaRepository;
import com.codifica.elevebot.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public String cadastrar(List<DespesaDTO> despesasDTO) {
        List<Despesa> despesas = despesasDTO.stream().map(dto -> {
            Produto produto = produtoRepository.findById(dto.getProdutoId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado. ID: " + dto.getProdutoId()));
            return DespesaAdapter.toEntity(dto, produto);
        }).collect(Collectors.toList());

        despesaRepository.saveAll(despesas);
        return "Despesas cadastradas com sucesso";
    }

    public List<DespesaDTO> listar() {
        List<Despesa> despesas = despesaRepository.findAll();

        return despesas.stream()
                .map(DespesaAdapter::toDTO)
                .collect(Collectors.toList());
    }

    public DespesaDTO buscarPorId(Integer id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada."));

        return DespesaAdapter.toDTO(despesa);
    }

    public String atualizar(Integer id, DespesaDTO despesaDTO) {
        Despesa despesaExistente = despesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada."));

        Produto produto = produtoRepository.findById(despesaDTO.getProdutoId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado. ID: " + despesaDTO.getProdutoId()));

        despesaExistente.setProduto(produto);
        despesaExistente.setValor(despesaDTO.getValor());
        despesaExistente.setData(despesaDTO.getData());
        despesaRepository.save(despesaExistente);
        return "Despesa atualizada com sucesso.";
    }

    public String deletar(Integer id) {
        if (!despesaRepository.existsById(id)) {
            throw new NotFoundException("Despesa não encontrada.");
        }

        despesaRepository.deleteById(id);
        return "Despesa deletada com sucesso.";
    }
}