package com.techchallenge.encomendas.application.services.morador;

import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.mapper.MoradorMapper;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorJaCadastradoException;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CadastrarMoradorServiceTest {

    @Mock
    private MoradorRepository moradorRepository;

    @Mock
    private MoradorMapper moradorMapper;

    @InjectMocks
    private CadastrarMoradorService cadastrarMoradorService;

    private MoradorDTO moradorDTO;
    private Morador morador;
    private Morador moradorSalvo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        moradorDTO = new MoradorDTO(1L, "João da Silva", "12345678900", "11999999999", "12");
        morador = new Morador(1L, "João da Silva", "12345678900", "11999999999", "12");
        moradorSalvo = new Morador(1L, "João da Silva", "12345678900", "11999999999", "12");
    }

    @Test
    void deveCadastrarMoradorComSucesso() {
        when(moradorRepository.existePorCpf("12345678900")).thenReturn(false);
        when(moradorMapper.toEntity(moradorDTO)).thenReturn(morador);
        when(moradorRepository.salvar(morador)).thenReturn(moradorSalvo);
        when(moradorMapper.toDTO(moradorSalvo)).thenReturn(moradorDTO);

        var resultado = cadastrarMoradorService.cadastrar(moradorDTO);

        assertNotNull(resultado);
        assertEquals("João da Silva", resultado.nome());
        verify(moradorRepository).salvar(morador);
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        when(moradorRepository.existePorCpf("12345678900")).thenReturn(true);

        var exception = assertThrows(MoradorJaCadastradoException.class,
                () -> cadastrarMoradorService.cadastrar(moradorDTO));

        assertEquals("Morador já cadastrado com o cpf: 12345678900", exception.getMessage());
        verify(moradorRepository, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        MoradorDTO dto = new MoradorDTO(1L, null, "12345678900", "11999999999", "12");

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cadastrarMoradorService.cadastrar(dto));

        assertEquals("O nome do morador é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        MoradorDTO dto = new MoradorDTO(1L, "   ", "12345678900", "101", "11999999999");

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cadastrarMoradorService.cadastrar(dto));

        assertEquals("O nome do morador é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoApartamentoForNulo() {
        MoradorDTO dto = new MoradorDTO(1L, "João", "12345678900", "11999999999", null);

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cadastrarMoradorService.cadastrar(dto));

        assertEquals("O número do apartamento é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoApartamentoForVazio() {
        MoradorDTO dto = new MoradorDTO(1L, "João", "12345678900", "11999999999", " ");

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cadastrarMoradorService.cadastrar(dto));

        assertEquals("O número do apartamento é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForNulo() {
        MoradorDTO dto = new MoradorDTO(1L, "João", "12345678900", null, "12");

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cadastrarMoradorService.cadastrar(dto));

        assertEquals("O telefone do morador é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForVazio() {
        MoradorDTO dto = new MoradorDTO(1L, "João", "12345678900", " ", "12");

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cadastrarMoradorService.cadastrar(dto));

        assertEquals("O telefone do morador é obrigatório", exception.getMessage());
    }
}
