package com.techchallenge.encomendas.domain.exceptions.morador;

public class MoradorNaoEncontradoApartamentoException extends RuntimeException {
    public MoradorNaoEncontradoApartamentoException(String apartamento) {
        super("Morador não encontrado pelo apartamento: " + apartamento + ".");
    }
}
