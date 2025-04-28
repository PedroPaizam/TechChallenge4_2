package com.techchallenge.encomendas.infrastructure.database;

import com.techchallenge.encomendas.domain.entities.Morador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoradorJpaRepository extends JpaRepository<Morador, Long> {
    Optional<Morador> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    Optional<Morador> findByApartamento(String apartamento);
}
