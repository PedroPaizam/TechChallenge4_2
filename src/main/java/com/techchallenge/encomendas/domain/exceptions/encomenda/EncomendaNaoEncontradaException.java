package com.techchallenge.encomendas.domain.exceptions.encomenda;

public class EncomendaNaoEncontradaException extends RuntimeException {
    public EncomendaNaoEncontradaException(Long id) {
        super("Encomenda com id " + id + " não encontrada.");
    }
}
