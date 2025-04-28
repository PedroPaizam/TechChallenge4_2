package com.techchallenge.encomendas.adapters.in;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.NotificacaoConfirmacaoDTO;
import com.techchallenge.encomendas.application.usecases.morador.NotificarMoradorUseCase;
import org.springframework.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EncomendaListener {

    private static final Logger logger = LoggerFactory.getLogger(EncomendaListener.class);

    private final NotificarMoradorUseCase notificarMoradorUseCase;

    public EncomendaListener(NotificarMoradorUseCase notificarMoradorUseCase) {
        this.notificarMoradorUseCase = notificarMoradorUseCase;
    }

    @Bean
    public Consumer<Message<EncomendaDTO>> processarEncomenda() {
        return message -> {
            EncomendaDTO encomendaDTO = message.getPayload();
            logger.info("Recebida mensagem de notificação para encomenda: {}", encomendaDTO.id());

            try {
                boolean notificado = notificarMoradorUseCase.notificar(encomendaDTO);

                if (notificado) {
                    logger.info("Morador {} notificado com sucesso para a encomenda: {}", encomendaDTO.morador().nome(), encomendaDTO.id());
                } else {
                    logger.warn("Não foi possível notificar o morador {} para a encomenda: {}", encomendaDTO.morador().nome(), encomendaDTO.id());
                }
            } catch (Exception e) {
                logger.error("Erro ao processar notificação para encomenda: {}", encomendaDTO.id(), e);
                throw e;
            }
        };
    }

    @Bean
    public Consumer<Message<EncomendaDTO>> confirmarRetirada() {
        return message -> {
            EncomendaDTO encomendaDTO = message.getPayload();
            logger.info("Recebida confirmação de retirada para encomenda: {}", encomendaDTO.id());

            try {
                boolean confirmado = notificarMoradorUseCase.confirmarNotificacao(encomendaDTO.id(), true);

                if (confirmado) {
                    logger.info("Confirmação de retirada registrada com sucesso para a encomenda: {}", encomendaDTO.id());
                } else {
                    logger.warn("Não foi possível registrar a confirmação de retirada para a encomenda: {}", encomendaDTO.id());
                }
            } catch (Exception e) {
                logger.error("Erro ao processar confirmação de retirada para encomenda: {}", encomendaDTO.id(), e);
                throw e;
            }
        };
    }

    @Bean
    public Consumer<Message<NotificacaoConfirmacaoDTO>> confirmarNotificacao() {
        return message -> {
            NotificacaoConfirmacaoDTO confirmacaoDTO = message.getPayload();
            logger.info("Recebida confirmação de notificação para encomenda: {}", confirmacaoDTO.encomendaId());

            try {
                boolean confirmado = notificarMoradorUseCase.confirmarNotificacao(
                        confirmacaoDTO.encomendaId(),
                        confirmacaoDTO.confirmado()
                );

                if (confirmado) {
                    logger.info("Confirmação de notificação registrada com sucesso para a encomenda: {}",
                            confirmacaoDTO.encomendaId());
                } else {
                    logger.warn("Não foi possível registrar a confirmação de notificação para a encomenda: {}",
                            confirmacaoDTO.encomendaId());
                }
            } catch (Exception e) {
                logger.error("Erro ao processar confirmação de notificação para encomenda: {}",
                        confirmacaoDTO.encomendaId(), e);
                throw e;
            }
        };
    }

}
