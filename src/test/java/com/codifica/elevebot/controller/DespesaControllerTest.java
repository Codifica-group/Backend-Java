package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.DespesaDTO;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.service.DespesaService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DespesaController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DespesaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DespesaService despesaService;

    private static DespesaDTO despesaPadrao;

    @Autowired
    private ObjectMapper mapper;

    private String asJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setUp() {
        despesaPadrao = new DespesaDTO();
        despesaPadrao.setId(1);
        despesaPadrao.setProdutoId(1);
        despesaPadrao.setValor(200.0);
        despesaPadrao.setData(LocalDate.of(2025, 4, 18));
    }

    /* -----------  POST  ----------- */
    @Test @Order(1)
    void deveCadastrarDespesas() throws Exception {
        String mensagem = "Despesas cadastradas com sucesso";
        Mockito.when(despesaService.cadastrar(anyList()))
                .thenReturn(mensagem);

        mvc.perform(post("/api/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(List.of(despesaPadrao))))
                .andExpect(status().isCreated())
                .andExpect(content().string(mensagem));
    }

    @Test @Order(2)
    void deveFalharNoCadastro_ProdutoInexistente() throws Exception {
        Mockito.when(despesaService.cadastrar(anyList()))
                .thenThrow(new NotFoundException("Produto não encontrado. ID: 99"));

        mvc.perform(post("/api/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(List.of(despesaPadrao))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Produto não encontrado. ID: 99"));
    }

    /* -----------  GET ALL  ----------- */
    @Test @Order(3)
    void deveListarDespesas() throws Exception {
        Mockito.when(despesaService.listar()).thenReturn(List.of(despesaPadrao));

        mvc.perform(get("/api/despesas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].valor").value(200.0));
    }

    @Test @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        Mockito.when(despesaService.listar()).thenReturn(List.of());

        mvc.perform(get("/api/despesas"))
                .andExpect(status().isNoContent());
    }

    /* -----------  GET BY ID  ----------- */
    @Test @Order(5)
    void deveBuscarDespesaPorId() throws Exception {
        Mockito.when(despesaService.buscarPorId(1)).thenReturn(despesaPadrao);

        mvc.perform(get("/api/despesas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(1))
                .andExpect(jsonPath("$.valor").value(200.0));
    }

    @Test @Order(6)
    void deveRetornar404_DespesaNaoEncontrada() throws Exception {
        Mockito.when(despesaService.buscarPorId(99))
                .thenThrow(new NotFoundException("Despesa não encontrada."));

        mvc.perform(get("/api/despesas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Despesa não encontrada."));
    }

    /* -----------  PUT  ----------- */
    @Test @Order(7)
    void deveAtualizarDespesa() throws Exception {
        Mockito.when(despesaService.atualizar(eq(1), any(DespesaDTO.class)))
                .thenReturn("{\"mensagem\": \"Despesa atualizada com sucesso.\"}");

        mvc.perform(put("/api/despesas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(despesaPadrao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Despesa atualizada com sucesso."));
    }

    @Test @Order(8)
    void deveFalharNaAtualizacao_DespesaNaoEncontrada() throws Exception {
        Mockito.when(despesaService.atualizar(eq(99), any(DespesaDTO.class)))
                .thenThrow(new NotFoundException("Despesa não encontrada."));

        mvc.perform(put("/api/despesas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(despesaPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Despesa não encontrada."));
    }

    @Test @Order(9)
    void deveFalharNaAtualizacao_ProdutoNaoEncontrado() throws Exception {
        Mockito.when(despesaService.atualizar(eq(1), any(DespesaDTO.class)))
                .thenThrow(new NotFoundException("Produto não encontrado. ID: 99"));

        mvc.perform(put("/api/despesas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(despesaPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Produto não encontrado. ID: 99"));
    }

    /* -----------  DELETE  ----------- */
    @Test @Order(10)
    void deveDeletarDespesa() throws Exception {
        Mockito.when(despesaService.deletar(1))
                .thenReturn("Despesa deletada com sucesso.");

        mvc.perform(delete("/api/despesas/1"))
                .andExpect(status().isNoContent());
    }

    @Test @Order(11)
    void deveFalharAoDeletar_DespesaNaoEncontrada() throws Exception {
        Mockito.when(despesaService.deletar(99))
                .thenThrow(new NotFoundException("Despesa não encontrada."));

        mvc.perform(delete("/api/despesas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Despesa não encontrada."));
    }
}