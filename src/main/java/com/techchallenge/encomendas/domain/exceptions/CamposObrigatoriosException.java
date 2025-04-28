package com.techchallenge.encomendas.domain.exceptions;

public class CamposObrigatoriosException extends RuntimeException {
    public CamposObrigatoriosException(String mensagem) {
        super(mensagem);
    }
}
