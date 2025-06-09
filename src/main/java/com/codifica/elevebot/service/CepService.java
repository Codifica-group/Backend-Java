package com.codifica.elevebot.service;

import com.codifica.elevebot.dto.CepDTO;
import com.codifica.elevebot.exception.InternalServerErrorException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.exception.IllegalArgumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    private static final String URL_VIACEP = System.getenv("URL_VIACEP");

    public CepDTO buscarCep(String cep) {
        validarCep(cep);

        try {
            RestTemplate restTemplate = new RestTemplate();
            CepDTO endereco = restTemplate.getForObject(URL_VIACEP, CepDTO.class, cep);

            if (endereco == null || Boolean.TRUE.equals(endereco.getErro())) {
                throw new NotFoundException("CEP não encontrado: " + cep);
            }

            return endereco;
        } catch (Exception e) {
            throw new InternalServerErrorException("Erro interno na API VIACEP: " + e);
        }
    }

    private void validarCep(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("O CEP deve conter 8 dígitos numéricos.");
        }
    }
}
