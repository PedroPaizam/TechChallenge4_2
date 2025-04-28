package com.techchallenge.encomendas.application.dto;

public record MoradorDTO(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String apartamento) {
}
