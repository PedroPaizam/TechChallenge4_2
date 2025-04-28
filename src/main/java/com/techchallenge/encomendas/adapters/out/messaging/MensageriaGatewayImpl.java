package com.techchallenge.encomendas.adapters.out.messaging;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.NotificacaoConfirmacaoDTO;
import com.techchallenge.encomendas.infrastructure.messaging.MensageriaGateway;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class MensageriaGatewayImpl implements MensageriaGateway {

    private final StreamBridge streamBridge;

    // Canais de saída
    private static final String NOTIFICACAO_OUTPUT = "notificarMorador-out-0";
    private static final String RETIRADA_OUTPUT = "confirmarRetirada-out-0";
    private static final String CONFIRMACAO_NOTIFICACAO_OUTPUT = "confirmarNotificacao-out-0";

    public MensageriaGatewayImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public boolean enviarParaFilaNotificacao(EncomendaDTO encomendaDTO) {
        return streamBridge.send(NOTIFICACAO_OUTPUT, encomendaDTO);
    }

    @Override
    public boolean enviarConfirmacaoRetirada(EncomendaDTO encomendaDTO) {
        return streamBridge.send(RETIRADA_OUTPUT, encomendaDTO);
    }

    @Override
    public boolean enviarConfirmacaoNotificacao(Long encomendaId, boolean confirmado) {
        NotificacaoConfirmacaoDTO confirmacaoDTO = new NotificacaoConfirmacaoDTO(encomendaId, confirmado);
        return streamBridge.send(CONFIRMACAO_NOTIFICACAO_OUTPUT, confirmacaoDTO);
    }
}
