package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.CepDTO;
import com.codifica.elevebot.exception.IllegalArgumentException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.service.CepService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CepController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CepControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CepService service;

    private static CepDTO cepPadrao;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        cepPadrao = new CepDTO();
        cepPadrao.setCep("01001000");
        cepPadrao.setLogradouro("Praça da Sé");
        cepPadrao.setComplemento("lado ímpar");
        cepPadrao.setBairro("Sé");
        cepPadrao.setLocalidade("São Paulo");
        cepPadrao.setUf("SP");
        cepPadrao.setIbge("3550308");
        cepPadrao.setGia("1004");
        cepPadrao.setDdd("11");
        cepPadrao.setSiafi("7107");
        cepPadrao.setErro(false);
    }

    // ---------- GET by CEP ----------
    @Test
    @Order(1)
    void deveBuscarCepComSucesso() throws Exception {
        when(service.buscarCep("01001000")).thenReturn(cepPadrao);

        mvc.perform(get("/api/cep/01001000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("01001000"))
                .andExpect(jsonPath("$.logradouro").value("Praça da Sé"))
                .andExpect(jsonPath("$.bairro").value("Sé"))
                .andExpect(jsonPath("$.localidade").value("São Paulo"))
                .andExpect(jsonPath("$.uf").value("SP"));
    }

    @Test
    @Order(2)
    void deveRetornar404_CepNaoEncontrado() throws Exception {
        when(service.buscarCep("99999999"))
                .thenThrow(new NotFoundException("CEP não encontrado: 99999999"));

        mvc.perform(get("/api/cep/99999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("CEP não encontrado: 99999999"));
    }

    @Test
    @Order(3)
    void deveRetornar400_CepInvalido() throws Exception {
        when(service.buscarCep("123"))
                .thenThrow(new IllegalArgumentException("O CEP deve conter 8 dígitos numéricos."));

        mvc.perform(get("/api/cep/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("O CEP deve conter 8 dígitos numéricos."));
    }

    @Test
    @Order(4)
    void deveRetornar500_CepEmFormatoNuloOuVazio() throws Exception {
        when(service.buscarCep(""))
                .thenThrow(new IllegalArgumentException("O CEP deve conter 8 dígitos numéricos."));

        mvc.perform(get("/api/cep/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
