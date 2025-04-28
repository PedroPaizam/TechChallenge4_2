package com.techchallenge.encomendas.infrastructure.messaging;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;

public interface MensageriaGateway {
    boolean enviarParaFilaNotificacao(EncomendaDTO encomendaDTO);
    boolean enviarConfirmacaoRetirada(EncomendaDTO encomendaDTO);
    boolean enviarConfirmacaoNotificacao(Long encomendaId, boolean confirmado);
}
