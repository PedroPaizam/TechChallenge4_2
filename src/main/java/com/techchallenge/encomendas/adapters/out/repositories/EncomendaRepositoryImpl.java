package com.techchallenge.encomendas.adapters.out.repositories;

import com.techchallenge.encomendas.domain.entities.Encomenda;
import com.techchallenge.encomendas.domain.repositories.EncomendaRepository;
import com.techchallenge.encomendas.infrastructure.database.EncomendaJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EncomendaRepositoryImpl implements EncomendaRepository {

    private final EncomendaJpaRepository encomendaJpaRepository;

    public EncomendaRepositoryImpl(EncomendaJpaRepository encomendaJpaRepository) {
        this.encomendaJpaRepository = encomendaJpaRepository;
    }

    @Override
    public Encomenda salvar(Encomenda encomenda) {
        return encomendaJpaRepository.save(encomenda);
    }

    @Override
    public Optional<Encomenda> buscarPorId(Long id) {
        return encomendaJpaRepository.findById(id);
    }

    @Override
    public List<Encomenda> buscarPorMorador(Long idMorador) {
        return encomendaJpaRepository.findByMoradorId(idMorador);
    }

}
