package com.techchallenge.encomendas.application.services.morador;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.domain.entities.Encomenda;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.enums.Status;
import com.techchallenge.encomendas.domain.repositories.EncomendaRepository;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import com.techchallenge.encomendas.infrastructure.messaging.MensageriaGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificarMoradorServiceTest {

    @Mock
    private EncomendaRepository encomendaRepository;

    @Mock
    private MoradorRepository moradorRepository;

    @Mock
    private MensageriaGateway mensageriaGateway;

    @InjectMocks
    private NotificarMoradorService notificarMoradorService;

    private Morador morador;
    private MoradorDTO moradorDTO;
    private Encomenda encomenda;
    private EncomendaDTO encomendaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        morador = new Morador(1L, "João da Silva", "12345678900", "11999999999", "12");
        moradorDTO = new MoradorDTO(1L, "João da Silva", "12345678900", "11999999999", "12");

        encomenda = new Encomenda(1L, morador, "Caixa da Amazon", Status.RECEBIDA, LocalDateTime.now(), null, null, null, "André Lima");
        encomendaDTO = new EncomendaDTO(1L, moradorDTO, "Caixa da Amazon", Status.RECEBIDA, LocalDateTime.now(), null, null, null, "André Lima");
    }

    @Test
    void deveNotificarMoradorComSucesso() {
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));
        when(moradorRepository.buscarPorId(1L)).thenReturn(Optional.of(morador));
        when(encomendaRepository.salvar(any(Encomenda.class))).thenReturn(encomenda);

        boolean resultado = notificarMoradorService.notificar(encomendaDTO);

        assertTrue(resultado);
        assertEquals(Status.NOTIFICADA, encomenda.getStatus());
        assertNotNull(encomenda.getDataNotificacao());
        verify(encomendaRepository).salvar(encomenda);
    }

    @Test
    void deveLancarExcecaoQuandoEncomendaNaoEncontrada() {
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> notificarMoradorService.notificar(encomendaDTO));

        assertEquals("Encomenda não encontrada: 1", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoMoradorNaoEncontrado() {
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));
        when(moradorRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> notificarMoradorService.notificar(encomendaDTO));

        assertEquals("Morador não encontrado: 1", exception.getMessage());
    }

    @Test
    void deveConfirmarNotificacaoComSucesso() {
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));
        when(encomendaRepository.salvar(any(Encomenda.class))).thenReturn(encomenda);

        boolean resultado = notificarMoradorService.confirmarNotificacao(1L, true);

        assertTrue(resultado);
        assertEquals(Status.NOTIFICACAO_CONFIRMADA, encomenda.getStatus());
        assertNotNull(encomenda.getDataConfirmacaoNotificacao());
        verify(encomendaRepository).salvar(encomenda);
    }

    @Test
    void naoDeveConfirmarNotificacaoQuandoNaoConfirmado() {
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));

        boolean resultado = notificarMoradorService.confirmarNotificacao(1L, false);

        assertFalse(resultado);
        verify(encomendaRepository, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoEncomendaNaoEncontradaAoConfirmarNotificacao() {
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> notificarMoradorService.confirmarNotificacao(1L, true));

        assertEquals("Encomenda não encontrada: 1", exception.getMessage());
    }
}
