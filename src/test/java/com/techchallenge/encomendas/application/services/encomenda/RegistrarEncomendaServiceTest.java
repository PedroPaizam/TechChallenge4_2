package com.techchallenge.encomendas.application.services.encomenda;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.dto.NovaEncomendaDTO;
import com.techchallenge.encomendas.application.mapper.EncomendaMapper;
import com.techchallenge.encomendas.domain.entities.Encomenda;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.enums.Status;
import com.techchallenge.encomendas.domain.exceptions.CamposObrigatoriosException;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaJaRetiradaException;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaNaoEncontradaException;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrarEncomendaServiceTest {

    @Mock
    private EncomendaRepository encomendaRepository;

    @Mock
    private MoradorRepository moradorRepository;

    @Mock
    private EncomendaMapper encomendaMapper;

    @Mock
    private MensageriaGateway mensageriaGateway;

    @InjectMocks
    private RegistrarEncomendaService registrarEncomendaService;

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
    void deveRegistrarEncomendaComMoradorId() {
        var novaEncomendaDTO = new NovaEncomendaDTO(1L, "12345678900", "Caixa da Amazon", "André Lima");

        when(moradorRepository.buscarPorId(1L)).thenReturn(Optional.of(morador));
        when(encomendaRepository.salvar(any(Encomenda.class))).thenReturn(encomenda);
        when(encomendaMapper.toDTO(any(Encomenda.class))).thenReturn(encomendaDTO);

        var resultado = registrarEncomendaService.registrar(novaEncomendaDTO);

        assertThat(resultado).isEqualTo(encomendaDTO);
        verify(mensageriaGateway).enviarParaFilaNotificacao(encomendaDTO);
    }

    @Test
    void deveRegistrarEncomendaComMoradorCpf() {
        var novaEncomendaDTO = new NovaEncomendaDTO(1L, "12345678900", "Caixa da Amazon", "André Lima");

        when(moradorRepository.buscarPorId(1L)).thenReturn(Optional.of(morador));
        when(moradorRepository.buscarPorCpf("12345678900")).thenReturn(Optional.of(morador));
        when(encomendaRepository.salvar(any())).thenReturn(encomenda);
        when(encomendaMapper.toDTO(any())).thenReturn(encomendaDTO);

        var resultado = registrarEncomendaService.registrar(novaEncomendaDTO);

        assertThat(resultado).isEqualTo(encomendaDTO);
        verify(mensageriaGateway).enviarParaFilaNotificacao(encomendaDTO);
    }

    @Test
    void deveLancarExcecaoSeNaoInformarCpfOuId() {
        var dto = new NovaEncomendaDTO(null, "", "Caixa da Amazon", "André Lima");

        assertThatThrownBy(() -> registrarEncomendaService.registrar(dto))
                .isInstanceOf(CamposObrigatoriosException.class)
                .hasMessageContaining("informar o ID ou CPF");
    }

    @Test
    void deveRegistrarRetiradaComSucesso() {
        encomenda.setStatus(Status.RECEBIDA);
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));
        when(encomendaRepository.salvar(any())).thenReturn(encomenda);
        when(encomendaMapper.toDTO(any())).thenReturn(encomendaDTO);

        var resultado = registrarEncomendaService.registrarRetirada(1L);

        assertThat(resultado).isEqualTo(encomendaDTO);
        verify(mensageriaGateway).enviarConfirmacaoRetirada(encomendaDTO);
    }

    @Test
    void deveLancarExcecaoSeEncomendaJaFoiRetirada() {
        encomenda.setStatus(Status.RETIRADA);
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));

        assertThatThrownBy(() -> registrarEncomendaService.registrarRetirada(1L))
                .isInstanceOf(EncomendaJaRetiradaException.class)
                .hasMessageContaining("1");
    }

    @Test
    void deveConfirmarNotificacaoComSucesso() {
        encomenda.setDataConfirmacaoNotificacao(null);
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));

        var resultado = registrarEncomendaService.confirmarNotificacao(1L);

        assertThat(resultado).isTrue();
        verify(encomendaRepository).salvar(encomenda);
    }

    @Test
    void deveRetornarTrueSeNotificacaoJaConfirmada() {
        encomenda.setDataConfirmacaoNotificacao(LocalDateTime.now());
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));

        var resultado = registrarEncomendaService.confirmarNotificacao(1L);

        assertThat(resultado).isTrue();
        verify(encomendaRepository, never()).salvar(encomenda);
    }

    @Test
    void deveLancarExcecaoSeEncomendaNaoForEncontrada() {
        when(encomendaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> registrarEncomendaService.confirmarNotificacao(99L))
                .isInstanceOf(EncomendaNaoEncontradaException.class)
                .hasMessageContaining("99");
    }
}
