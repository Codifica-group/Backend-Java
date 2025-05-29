package com.codifica.elevebot.controller;

import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.service.CategoriaProdutoService;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoriaProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoriaProdutoService categoriaProdutoService;

    private static CategoriaProduto categoriaPadrao;
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
        categoriaPadrao = new CategoriaProduto("Gasto Fixo");
        categoriaPadrao.setId(1);
    }

    /* -----------  POST  ----------- */
    @Test
    @Order(1)
    void deveCadastrarCategoria() throws Exception {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Categoria cadastrado com sucesso.");
        resposta.put("id", 1);

        Mockito.when(categoriaProdutoService.cadastrar(any(CategoriaProduto.class))).thenReturn(resposta);

        mvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(categoriaPadrao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Categoria cadastrado com sucesso."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    void deveFalharNoCadastro_CategoriaDuplicada() throws Exception {
        Mockito.when(categoriaProdutoService.cadastrar(any(CategoriaProduto.class)))
                .thenThrow(new ConflictException("Não é possível cadastrar duas categorias iguais."));

        mvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(categoriaPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Não é possível cadastrar duas categorias iguais."));
    }

    /* -----------  GET  ----------- */
    @Test
    @Order(3)
    void deveListarCategorias() throws Exception {
        List<CategoriaProduto> categorias = Arrays.asList(categoriaPadrao);

        Mockito.when(categoriaProdutoService.listar()).thenReturn(categorias);

        mvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Gasto Fixo"));
    }

    @Test
    @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        Mockito.when(categoriaProdutoService.listar()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/categorias"))
                .andExpect(status().isNoContent());
    }

    /* -----------  PUT  ----------- */
    @Test
    @Order(5)
    void deveAtualizarCategoria() throws Exception {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Categoria atualizada com sucesso.");
        resposta.put("id", 1);

        Mockito.when(categoriaProdutoService.atualizar(eq(1), any(CategoriaProduto.class))).thenReturn(resposta);

        mvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(categoriaPadrao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Categoria atualizada com sucesso."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(6)
    void deveFalharNaAtualizacao_CategoriaDuplicada() throws Exception {
        Mockito.when(categoriaProdutoService.atualizar(eq(1), any(CategoriaProduto.class)))
                .thenThrow(new ConflictException("Uma categoria com este nome já existe."));

        mvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(categoriaPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Uma categoria com este nome já existe."));
    }

    @Test
    @Order(7)
    void deveFalharNaAtualizacao_CategoriaInexistente() throws Exception {
        Mockito.when(categoriaProdutoService.atualizar(eq(99), any(CategoriaProduto.class)))
                .thenThrow(new NotFoundException("Categoria não encontrada."));

        mvc.perform(put("/api/categorias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(categoriaPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Categoria não encontrada."));
    }

    /* -----------  DELETE  ----------- */
    @Test
    @Order(8)
    void deveDeletarCategoria() throws Exception {
        Mockito.doNothing().when(categoriaProdutoService).deletar(1);

        mvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(9)
    void deveFalharAoDeletar_CategoriaComProdutos() throws Exception {
        Mockito.doThrow(new ConflictException("Não é possível deletar categorias que possuem produtos cadastrados."))
                .when(categoriaProdutoService).deletar(1);

        mvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Não é possível deletar categorias que possuem produtos cadastrados."));
    }

    @Test
    @Order(10)
    void deveFalharAoDeletar_CategoriaInexistente() throws Exception {
        Mockito.doThrow(new NotFoundException("Categoria não encontrada."))
                .when(categoriaProdutoService).deletar(99);

        mvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Categoria não encontrada."));
    }
}