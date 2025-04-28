package com.techchallenge.encomendas.domain.exceptions.morador;

public class MoradorJaCadastradoException extends RuntimeException {
    public MoradorJaCadastradoException(String cpf) {
        super("Morador já cadastrado com o cpf: " + cpf);
    }
}
