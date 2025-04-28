package com.techchallenge.encomendas.adapters.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.usecases.morador.BuscarMoradorUseCase;
import com.techchallenge.encomendas.application.usecases.morador.CadastrarMoradorUseCase;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MoradorController.class)
class MoradorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CadastrarMoradorUseCase cadastrarMoradorUseCase;

    @MockBean
    private BuscarMoradorUseCase buscarMoradorUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private final MoradorDTO moradorDTO = new MoradorDTO(1L, "João da Silva", "12345678900", "11999999999", "12");

    @Test
    void deveCadastrarMoradorComSucesso() throws Exception {
        Mockito.when(cadastrarMoradorUseCase.cadastrar(any())).thenReturn(moradorDTO);

        mockMvc.perform(post("/api/moradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moradorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void deveBuscarMoradorPorId() throws Exception {
        Mockito.when(buscarMoradorUseCase.buscarPorId(1L)).thenReturn(moradorDTO);

        mockMvc.perform(get("/api/moradores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void deveBuscarMoradorPorCpf() throws Exception {
        Mockito.when(buscarMoradorUseCase.buscarPorCpf("12345678900")).thenReturn(moradorDTO);

        mockMvc.perform(get("/api/moradores")
                        .param("cpf", "12345678900"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void deveBuscarMoradorPorApartamento() throws Exception {
        Mockito.when(buscarMoradorUseCase.buscarPorApartamento("101")).thenReturn(moradorDTO);

        mockMvc.perform(get("/api/moradores")
                        .param("apartamento", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apartamento").value("12"));
    }

    @Test
    void deveListarTodosMoradores() throws Exception {
        Mockito.when(buscarMoradorUseCase.listarTodosMoradores()).thenReturn(List.of(moradorDTO));

        mockMvc.perform(get("/api/moradores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void deveRetornarNoContentQuandoNaoHouverMoradores() throws Exception {
        Mockito.when(buscarMoradorUseCase.listarTodosMoradores()).thenReturn(List.of());

        mockMvc.perform(get("/api/moradores"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundQuandoMoradorNaoExiste() throws Exception {
        Mockito.when(buscarMoradorUseCase.buscarPorId(999L))
                .thenThrow(new MoradorNaoEncontradoException("Morador com ID 999 não encontrado."));

        mockMvc.perform(get("/api/moradores/999"))
                .andExpect(status().isNotFound());
    }
}
