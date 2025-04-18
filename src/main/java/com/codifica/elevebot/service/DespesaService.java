package com.codifica.elevebot.service;

import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Despesa;
import com.codifica.elevebot.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    public String cadastrar(List<Despesa> despesas) {
        despesaRepository.saveAll(despesas);
        return "Despesas cadastradas com sucesso";
    }

    public List<Despesa> listar() {
        return despesaRepository.findAll();
    }

    public Despesa buscarPorId(Integer id) {
        return despesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada."));
    }

    public String atualizar(Integer id, Despesa despesa) {
        if (!despesaRepository.existsById(id)) {
            throw new NotFoundException("Despesa não encontrada.");
        }

        despesa.setId(id);
        despesaRepository.save(despesa);
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
