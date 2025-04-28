package com.techchallenge.encomendas.application.services.morador;

import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.mapper.MoradorMapper;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoException;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BuscarMoradorServiceTest {

    @Mock
    private MoradorRepository moradorRepository;

    @Mock
    private MoradorMapper moradorMapper;

    @InjectMocks
    private BuscarMoradorService buscarMoradorService;

    private Morador morador;
    private MoradorDTO moradorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        morador = new Morador(1L, "João da Silva", "12345678900", "101", "12");
        moradorDTO = new MoradorDTO(1L, "João da Silva", "12345678900", "101", "12");
    }

    @Test
    void deveBuscarMoradorPorIdComSucesso() {
        when(moradorRepository.buscarPorId(1L)).thenReturn(Optional.of(morador));
        when(moradorMapper.toDTO(morador)).thenReturn(moradorDTO);

        var resultado = buscarMoradorService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("João da Silva", resultado.nome());
        verify(moradorRepository).buscarPorId(1L);
    }

    @Test
    void deveBuscarMoradorPorCpfComSucesso() {
        when(moradorRepository.buscarPorCpf("12345678900")).thenReturn(Optional.of(morador));
        when(moradorMapper.toDTO(morador)).thenReturn(moradorDTO);

        var resultado = buscarMoradorService.buscarPorCpf("12345678900");

        assertEquals("João da Silva", resultado.nome());
        verify(moradorRepository).buscarPorCpf("12345678900");
    }

    @Test
    void deveRetornarMoradorDTOQuandoBuscarPorIdExistente() {
        when(moradorRepository.buscarPorId(1L)).thenReturn(Optional.of(morador));
        when(moradorMapper.toDTO(morador)).thenReturn(moradorDTO);

        var resultado = buscarMoradorService.buscarPorId(1L);

        assertThat(resultado).isEqualTo(moradorDTO);
        verify(moradorRepository).buscarPorId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorIdInexistente() {
        when(moradorRepository.buscarPorId(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarMoradorService.buscarPorId(2L))
                .isInstanceOf(MoradorNaoEncontradoException.class)
                .hasMessageContaining("2");
    }

    @Test
    void deveRetornarMoradorDTOQuandoBuscarPorCpfExistente() {
        when(moradorRepository.buscarPorCpf("12345678900")).thenReturn(Optional.of(morador));
        when(moradorMapper.toDTO(morador)).thenReturn(moradorDTO);

        var resultado = buscarMoradorService.buscarPorCpf("12345678900");

        assertThat(resultado).isEqualTo(moradorDTO);
    }

    @Test
    void deveBuscarMoradorPorApartamentoComSucesso() {
        when(moradorRepository.buscarPorApartamento("12")).thenReturn(Optional.of(morador));
        when(moradorMapper.toDTO(morador)).thenReturn(moradorDTO);

        var resultado = buscarMoradorService.buscarPorApartamento("12");

        assertEquals("12", resultado.apartamento());
        verify(moradorRepository).buscarPorApartamento("12");
    }

    @Test
    void deveListarTodosMoradoresComSucesso() {
        when(moradorRepository.listarTodosMoradores()).thenReturn(List.of(morador));
        when(moradorMapper.toDTO(morador)).thenReturn(moradorDTO);

        var resultado = buscarMoradorService.listarTodosMoradores();

        assertEquals(1, resultado.size());
        assertEquals("João da Silva", resultado.get(0).nome());
    }
}
