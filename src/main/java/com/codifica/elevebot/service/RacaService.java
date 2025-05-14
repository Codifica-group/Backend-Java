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

    public Map<String, List<RacaDTO>> listar() {
        List<Raca> racas = racaRepository.findAll();

        List<RacaDTO> listaDTO = racas.stream()
                .map(racaAdapter::toDTO)
                .collect(Collectors.toList());

        Map<String, List<RacaDTO>> racasAgrupadas = listaDTO.stream()
                .collect(Collectors.groupingBy(RacaDTO::getPorteNome));

        Map<String, List<RacaDTO>> resultadoOrdenado = new LinkedHashMap<>();
        if (racasAgrupadas.containsKey("Pequeno")) {
            resultadoOrdenado.put("Pequeno", racasAgrupadas.get("Pequeno"));
        }
        if (racasAgrupadas.containsKey("Médio")) {
            resultadoOrdenado.put("Médio", racasAgrupadas.get("Médio"));
        }
        if (racasAgrupadas.containsKey("Grande")) {
            resultadoOrdenado.put("Grande", racasAgrupadas.get("Grande"));
        }

        return resultadoOrdenado;
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

    public String deletar(Integer id) {
        if (!racaRepository.existsById(id)) {
            throw new NotFoundException("Raça não encontrada.");
        }

        if (petRepository.existsByRacaId(id)) {
            throw new ConflictException("Não é possível deletar raça que possui pets cadastrados.");
        }

        racaRepository.deleteById(id);
        return "Raça deletada com sucesso.";
    }
}
