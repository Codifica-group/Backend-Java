package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.RacaAdapter;
import com.codifica.elevebot.dto.RacaDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Porte;
import com.codifica.elevebot.model.Raca;
import com.codifica.elevebot.repository.PetRepository;
import com.codifica.elevebot.repository.RacaRepository;
import com.codifica.elevebot.repository.PorteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RacaService {

    @Autowired
    private RacaRepository racaRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PorteRepository porteRepository;

    @Autowired
    private RacaAdapter racaAdapter;

    public Object cadastrar(RacaDTO racaDTO) {
        if (racaRepository.existsByNome(racaDTO.getNome())) {
            throw new ConflictException("Raça já cadastrada.");
        }

        Porte porte = porteRepository.findById(racaDTO.getPorteId())
                .orElseThrow(() -> new NotFoundException("Porte não encontrado."));

        Raca raca = racaAdapter.toEntity(racaDTO, porte);
        racaRepository.save(raca);

        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Raça cadastrada com sucesso.");
        resposta.put("id", raca.getId());
        return resposta;
    }

    public List<RacaDTO> listar() {
        List<Raca> racas = racaRepository.findAll();

        return racas.stream()
                .map(racaAdapter::toDTO)
                .collect(Collectors.toList());
    }

    public String atualizar(Integer id, RacaDTO racaDTO) {
        Raca racaExistente = racaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Raça não encontrada."));

        Porte porte = porteRepository.findById(racaDTO.getPorteId())
                .orElseThrow(() -> new NotFoundException("Porte não encontrado."));

        racaExistente.setNome(racaDTO.getNome());
        racaExistente.setPorte(porte);
        racaRepository.save(racaExistente);
        return "Raça atualizada com sucesso.";
    }

    public void deletar(Integer id) {
        if (!racaRepository.existsById(id)) {
            throw new NotFoundException("Raça não encontrada.");
        }

        if (petRepository.existsByRacaId(id)) {
            throw new ConflictException("Não é possível deletar raça que possui pets cadastrados.");
        }

        racaRepository.deleteById(id);
    }
}
