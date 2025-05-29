package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.service.ProdutoService;
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

@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProdutoService produtoService;

    private static ProdutoDTO produtoPadrao;
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
        produtoPadrao = new ProdutoDTO();
        produtoPadrao.setNome("Aluguel");
        produtoPadrao.setCategoriaId(1);
        produtoPadrao.setId(1);
    }

    /* -----------  POST  ----------- */
    @Test @Order(1)
    void deveCadastrarProdutos() throws Exception {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Produtos cadastrados com sucesso.");
        resposta.put("id", 1);

        Mockito.when(produtoService.cadastrar(Mockito.anyList()))
                .thenReturn(resposta);

        mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(List.of(produtoPadrao))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Produtos cadastrados com sucesso."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test @Order(2)
    void deveFalharNoCadastro_ProdutoDuplicado() throws Exception {
        Mockito.when(produtoService.cadastrar(Mockito.anyList()))
                .thenThrow(new ConflictException(
                        "Não é possível cadastrar dois produtos iguais."));

        mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(List.of(produtoPadrao))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(
                        "Não é possível cadastrar dois produtos iguais."));
    }

    /* -----------  GET  ----------- */
    @Test @Order(3)
    void deveListarProdutos() throws Exception {
        List<ProdutoDTO> resposta = List.of(produtoPadrao);
        Mockito.when(produtoService.listar()).thenReturn(resposta);

        mvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Aluguel"));
    }

    @Test @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        Mockito.when(produtoService.listar()).thenReturn(List.of());

        mvc.perform(get("/api/produtos"))
                .andExpect(status().isNoContent());
    }

    /* -----------  PUT  ----------- */
    @Test @Order(5)
    void deveAtualizarProduto() throws Exception {
        Mockito.when(produtoService.atualizar(eq(1), any(ProdutoDTO.class)))
                .thenReturn("Produto atualizado com sucesso.");

        mvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(produtoPadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Produto atualizado com sucesso."));
    }

    @Test @Order(6)
    void deveFalharNaAtualizacao_ProdutoPossuiDespesas() throws Exception {
        Mockito.when(produtoService.atualizar(eq(1), any(ProdutoDTO.class)))
                .thenThrow(new ConflictException(
                        "Não é possível atualizar produtos que possuem despesas cadastradas."));

        mvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(produtoPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(
                        "Não é possível atualizar produtos que possuem despesas cadastradas."));
    }

    @Test @Order(7)
    void deveFalharNaAtualizacao_ProdutoInexistente() throws Exception {
        Mockito.when(produtoService.atualizar(eq(99), any(ProdutoDTO.class)))
                .thenThrow(new NotFoundException("Produto não encontrado."));

        mvc.perform(put("/api/produtos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(produtoPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Produto não encontrado."));
    }

    /* -----------  DELETE  ----------- */
    @Test @Order(8)
    void deveDeletarProduto() throws Exception {
        Mockito.doNothing().when(produtoService).deletar(1);

        mvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @Test @Order(9)
    void deveFalharAoDeletar_ProdutoInexistente() throws Exception {
        Mockito.doThrow(new NotFoundException("Produto não encontrado."))
                .when(produtoService).deletar(99);

        mvc.perform(delete("/api/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Produto não encontrado."));
    }
}