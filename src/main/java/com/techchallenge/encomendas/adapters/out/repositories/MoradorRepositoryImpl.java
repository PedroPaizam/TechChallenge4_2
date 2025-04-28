package com.techchallenge.encomendas.adapters.out.repositories;

import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import com.techchallenge.encomendas.infrastructure.database.MoradorJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MoradorRepositoryImpl implements MoradorRepository {

    private final MoradorJpaRepository moradorJpaRepository;

    public MoradorRepositoryImpl(MoradorJpaRepository moradorJpaRepository) {
        this.moradorJpaRepository = moradorJpaRepository;
    }

    @Override
    public Morador salvar(Morador morador) {
        return moradorJpaRepository.save(morador);
    }

    @Override
    public Optional<Morador> buscarPorId(Long id) {
        return moradorJpaRepository.findById(id);
    }

    @Override
    public Optional<Morador> buscarPorCpf(String cpf) {
        return moradorJpaRepository.findByCpf(cpf);
    }

    @Override
    public Optional<Morador> buscarPorApartamento(String numeroApartamento) {
        return moradorJpaRepository.findByApartamento(numeroApartamento);
    }

    @Override
    public List<Morador> listarTodosMoradores() {
        return moradorJpaRepository.findAll();
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return moradorJpaRepository.existsByCpf(cpf);
    }
}
