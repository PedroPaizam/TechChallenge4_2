package com.techchallenge.encomendas.application.dto;

public record NovaEncomendaDTO(
        Long moradorId,
        String moradorCpf,
        String descricao,
        String recebidaPor
) {
}
