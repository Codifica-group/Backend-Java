package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.ClienteDTO;
import com.codifica.elevebot.exception.*;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClienteService service;

    private static Cliente clientePadrao;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static String asJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setUp() {
        clientePadrao = new Cliente();
        clientePadrao.setId(1);
        clientePadrao.setNome("Mariana Souza");
        clientePadrao.setNumCelular("11912345678");
        clientePadrao.setCep("12345678");
        clientePadrao.setNumEndereco(1);
        clientePadrao.setComplemento("");
    }

    // ---------- POST ----------
    @Test
    @Order(1)
    void deveCadastrarCliente() throws Exception {
        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Cliente cadastrado com sucesso.");
        resposta.put("id", 1);

        when(service.cadastrar(any(Cliente.class))).thenReturn(resposta);

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(clientePadrao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Cliente cadastrado com sucesso."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    void deveFalharNoCadastro_ClienteJaExiste() throws Exception {
        when(service.cadastrar(any(Cliente.class)))
                .thenThrow(new ConflictException("Cliente já cadastrado."));

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(clientePadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Cliente já cadastrado."));
    }

    // ---------- GET ALL ----------
    @Test
    @Order(3)
    void deveListarClientes() throws Exception {
        when(service.listar()).thenReturn(List.of(new ClienteDTO()));

        mvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        when(service.listar()).thenReturn(List.of());

        mvc.perform(get("/api/clientes"))
                .andExpect(status().isNoContent());
    }

    // ---------- GET by ID ----------
    @Test
    @Order(5)
    void deveBuscarClientePorId() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(1);
        clienteDTO.setCep("12345678");

        when(service.buscarPorId(1)).thenReturn(clienteDTO);

        mvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cep").value("12345678"));
    }

    @Test
    @Order(6)
    void deveRetornar404_ClienteNaoEncontrado() throws Exception {
        when(service.buscarPorId(99))
                .thenThrow(new NotFoundException("Cliente não encontrado."));

        mvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado."));
    }

    // ---------- PUT ----------
    @Test
    @Order(7)
    void deveAtualizarCliente() throws Exception {
        when(service.atualizar(Mockito.eq(1), any(Cliente.class)))
                .thenReturn("Cliente atualizado com sucesso!");

        mvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(clientePadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente atualizado com sucesso!"));
    }

    @Test
    @Order(8)
    void deveFalharNaAtualizacao_ClienteInexistente() throws Exception {
        when(service.atualizar(Mockito.eq(99), any(Cliente.class)))
                .thenThrow(new NotFoundException("Cliente não encontrado."));

        mvc.perform(put("/api/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(clientePadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado."));
    }

    // ---------- DELETE ----------
    @Test
    @Order(9)
    void deveDeletarCliente() throws Exception {
        mvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(10)
    void deveFalharAoDeletar_ClientePossuiPets() throws Exception {
        Mockito.doThrow(new ConflictException("Não é possível deletar clientes que possui pets cadastrados."))
                        .when(service).deletar(1);

        mvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(
                        "Não é possível deletar clientes que possui pets cadastrados."));
    }

    @Test
    @Order(11)
    void deveFalharAoDeletar_ClienteInexistente() throws Exception {
        Mockito.doThrow(new NotFoundException("Cliente não encontrado."))
                .when(service).deletar(99);

        mvc.perform(delete("/api/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado."));
    }
}
