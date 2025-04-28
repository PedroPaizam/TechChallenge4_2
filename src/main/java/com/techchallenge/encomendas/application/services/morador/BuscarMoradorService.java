package com.techchallenge.encomendas.application.services.morador;

import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.mapper.MoradorMapper;
import com.techchallenge.encomendas.application.usecases.morador.BuscarMoradorUseCase;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoApartamentoException;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoException;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarMoradorService implements BuscarMoradorUseCase {

    private final MoradorRepository moradorRepository;
    private final MoradorMapper moradorMapper;

    public BuscarMoradorService(MoradorRepository moradorRepository,
                                MoradorMapper moradorMapper) {
        this.moradorRepository = moradorRepository;
        this.moradorMapper = moradorMapper;
    }

    @Override
    public MoradorDTO buscarPorId(Long id) {
        return moradorRepository.buscarPorId(id)
                .map(moradorMapper::toDTO)
                .orElseThrow(() -> new MoradorNaoEncontradoException(id));
    }

    @Override
    public MoradorDTO buscarPorCpf(String cpf) {
        return moradorRepository.buscarPorCpf(cpf)
                .map(moradorMapper::toDTO)
                .orElseThrow(() -> new MoradorNaoEncontradoException(cpf));
    }

    @Override
    public MoradorDTO buscarPorApartamento(String numeroApartamento) {
        return moradorRepository.buscarPorApartamento(numeroApartamento)
                .map(moradorMapper::toDTO)
                .orElseThrow(() -> new MoradorNaoEncontradoApartamentoException(numeroApartamento));
    }

    @Override
    public List<MoradorDTO> listarTodosMoradores() {
        return moradorRepository.listarTodosMoradores().stream()
                .map(moradorMapper::toDTO)
                .toList();
    }
}
