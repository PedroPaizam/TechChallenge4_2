package com.techchallenge.encomendas.domain.exceptions.encomenda;

public class EncomendaJaRetiradaException extends RuntimeException {
    public EncomendaJaRetiradaException(Long id) {
        super("Encomenda com id: " + id + " já foi retirada.");
    }
}
