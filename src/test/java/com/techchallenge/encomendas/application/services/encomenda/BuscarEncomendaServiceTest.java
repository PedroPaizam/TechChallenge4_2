package com.techchallenge.encomendas.application.services.encomenda;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.mapper.EncomendaMapper;
import com.techchallenge.encomendas.domain.entities.Encomenda;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.enums.Status;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaNaoEncontradaException;
import com.techchallenge.encomendas.domain.repositories.EncomendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BuscarEncomendaServiceTest {

    @Mock
    private EncomendaRepository encomendaRepository;

    @Mock
    private EncomendaMapper encomendaMapper;

    @InjectMocks
    private BuscarEncomendaService buscarEncomendaService;

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
    void deveBuscarEncomendaPorIdComSucesso() {
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));
        when(encomendaMapper.toDTO(encomenda)).thenReturn(encomendaDTO);

        var resultado = buscarEncomendaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        verify(encomendaRepository).buscarPorId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoEncomendaNaoEncontradaPorId() {
        when(encomendaRepository.buscarPorId(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarEncomendaService.buscarPorId(2L))
                .isInstanceOf(EncomendaNaoEncontradaException.class)
                .hasMessageContaining("2");

        verify(encomendaRepository).buscarPorId(2L);
    }

    @Test
    void deveBuscarEncomendasPorMoradorComSucesso() {
        when(encomendaRepository.buscarPorMorador(1L)).thenReturn(List.of(encomenda));
        when(encomendaMapper.toDTO(encomenda)).thenReturn(encomendaDTO);

        var resultado = buscarEncomendaService.buscarPorMorador(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(encomendaDTO);
        verify(encomendaRepository).buscarPorMorador(1L);
        verify(encomendaMapper).toDTO(encomenda);
    }
}
