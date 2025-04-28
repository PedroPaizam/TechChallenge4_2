package com.techchallenge.encomendas.application.usecases.morador;

import com.techchallenge.encomendas.application.dto.MoradorDTO;

import java.util.List;

public interface BuscarMoradorUseCase {
    MoradorDTO buscarPorId(Long id);
    MoradorDTO buscarPorCpf(String cpf);
    MoradorDTO buscarPorApartamento(String apartamento);
    List<MoradorDTO> listarTodosMoradores();
}
