package com.techchallenge.encomendas.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techchallenge.encomendas.domain.enums.Status;

import java.time.LocalDateTime;

public record EncomendaDTO(
        Long id,
        MoradorDTO morador,
        String descricao,
        Status status,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime dataRecebimento,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime dataNotificacao,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime dataConfirmacaoNotificacao,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime dataRetirada,
        String recebidaPor
) {
}
