package com.techchallenge.encomendas.domain.repositories;

import com.techchallenge.encomendas.domain.entities.Encomenda;

import java.util.List;
import java.util.Optional;

public interface EncomendaRepository {
    Encomenda salvar(Encomenda encomenda);
    Optional<Encomenda> buscarPorId(Long id);
    List<Encomenda> buscarPorMorador(Long idMorador);
}
