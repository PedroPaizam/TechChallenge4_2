package com.techchallenge.encomendas.domain.exceptions.morador;

public class MoradorNaoEncontradoException extends RuntimeException {
    public MoradorNaoEncontradoException(Long id) {
        super("Morador com id: " + id + " não encontrado.");
    }

    public MoradorNaoEncontradoException(String cpf) {
        super("Morador com cpf: " + cpf + " não encontrado.");
    }
}
