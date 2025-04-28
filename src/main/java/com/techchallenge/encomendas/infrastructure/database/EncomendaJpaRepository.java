package com.techchallenge.encomendas.infrastructure.database;

import com.techchallenge.encomendas.domain.entities.Encomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncomendaJpaRepository extends JpaRepository<Encomenda, Long> {
    List<Encomenda> findByMoradorId(Long moradorId);
}
