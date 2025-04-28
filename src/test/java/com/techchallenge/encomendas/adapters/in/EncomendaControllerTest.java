package com.techchallenge.encomendas.adapters.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.dto.NovaEncomendaDTO;
import com.techchallenge.encomendas.application.usecases.encomenda.BuscarEncomendaUseCase;
import com.techchallenge.encomendas.application.usecases.encomenda.RegistrarEncomendaUseCase;
import com.techchallenge.encomendas.domain.enums.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EncomendaController.class)
class EncomendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrarEncomendaUseCase registrarEncomendaUseCase;

    @MockBean
    private BuscarEncomendaUseCase buscarEncomendaUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private final MoradorDTO moradorDTO = new MoradorDTO(1L, "João da Silva", "12345678900", "11999999999", "12");
    private final EncomendaDTO encomendaDTO = new EncomendaDTO(1L, moradorDTO, "Caixa da Amazon", Status.RECEBIDA, LocalDateTime.now(), null, null, null, "André Lima");

    @Test
    void deveRegistrarEncomendaComSucesso() throws Exception {
        NovaEncomendaDTO novaEncomendaDTO = new NovaEncomendaDTO(1L, "12345678900", "Caixa da Amazon", "André Lima");

        Mockito.when(registrarEncomendaUseCase.registrar(any())).thenReturn(encomendaDTO);

        mockMvc.perform(post("/api/encomendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaEncomendaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(Status.RECEBIDA.name()))
                .andExpect(jsonPath("$.descricao").value("Caixa da Amazon"))
                .andExpect(jsonPath("$.recebidaPor").value("André Lima"));
    }

    @Test
    void deveBuscarEncomendaPorId() throws Exception {
        Mockito.when(buscarEncomendaUseCase.buscarPorId(1L)).thenReturn(encomendaDTO);

        mockMvc.perform(get("/api/encomendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveBuscarEncomendasPorMorador() throws Exception {
        Mockito.when(buscarEncomendaUseCase.buscarPorMorador(1L)).thenReturn(List.of(encomendaDTO));

        mockMvc.perform(get("/api/encomendas/morador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].morador.id").value(1L))
                .andExpect(jsonPath("$[0].morador.nome").value("João da Silva"))
                .andExpect(jsonPath("$[0].descricao").value("Caixa da Amazon"));
    }

    @Test
    void deveRegistrarRetiradaDeEncomenda() throws Exception {
        Mockito.when(registrarEncomendaUseCase.registrarRetirada(1L)).thenReturn(encomendaDTO);

        mockMvc.perform(put("/api/encomendas/1/retirada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveConfirmarNotificacaoComSucesso() throws Exception {
        Mockito.when(registrarEncomendaUseCase.confirmarNotificacao(1L)).thenReturn(true);
        Mockito.when(buscarEncomendaUseCase.buscarPorId(1L)).thenReturn(encomendaDTO);

        mockMvc.perform(put("/api/encomendas/1/confirmar-notificacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveRetornarErroAoConfirmarNotificacaoSemSucesso() throws Exception {
        Mockito.when(registrarEncomendaUseCase.confirmarNotificacao(1L)).thenReturn(false);

        mockMvc.perform(put("/api/encomendas/1/confirmar-notificacao"))
                .andExpect(status().isInternalServerError());
    }
}
