package com.techchallenge.encomendas.domain.repositories;

import com.techchallenge.encomendas.domain.entities.Morador;

import java.util.List;
import java.util.Optional;

public interface MoradorRepository {
    Morador salvar(Morador morador);
    Optional<Morador> buscarPorId(Long id);
    Optional<Morador> buscarPorCpf(String cpf);
    Optional<Morador> buscarPorApartamento(String numeroApartamento);
    List<Morador> listarTodosMoradores();
    boolean existePorCpf(String cpf);
}
