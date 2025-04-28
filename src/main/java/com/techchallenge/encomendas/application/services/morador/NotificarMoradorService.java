package com.techchallenge.encomendas.application.services.morador;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.usecases.morador.NotificarMoradorUseCase;
import com.techchallenge.encomendas.domain.entities.Encomenda;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.enums.Status;
import com.techchallenge.encomendas.domain.repositories.EncomendaRepository;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import com.techchallenge.encomendas.infrastructure.messaging.MensageriaGateway;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class NotificarMoradorService implements NotificarMoradorUseCase {

    private final EncomendaRepository encomendaRepository;
    private final MoradorRepository moradorRepository;
    private final MensageriaGateway mensageriaGateway;

    public NotificarMoradorService(EncomendaRepository encomendaRepository,
                                   MoradorRepository moradorRepository,
                                   MensageriaGateway mensageriaGateway) {
        this.encomendaRepository = encomendaRepository;
        this.moradorRepository = moradorRepository;
        this.mensageriaGateway = mensageriaGateway;
    }

    @Override
    @Transactional
    public boolean notificar(EncomendaDTO encomendaDTO) {
        Encomenda encomenda = encomendaRepository.buscarPorId(encomendaDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada: " + encomendaDTO.id()));

        Morador morador = moradorRepository.buscarPorId(encomenda.getMorador().getId())
                .orElseThrow(() -> new IllegalArgumentException("Morador não encontrado: " + encomenda.getMorador().getId()));

        boolean notificacaoEnviada = enviarNotificacao(morador, encomenda);

        if (notificacaoEnviada) {
            encomenda.setStatus(Status.NOTIFICADA);
            encomenda.setDataNotificacao(LocalDateTime.now());
            encomendaRepository.salvar(encomenda);

            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean confirmarNotificacao(Long encomendaId, boolean confirmado) {
        Encomenda encomenda = encomendaRepository.buscarPorId(encomendaId)
                .orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada: " + encomendaId));

        if (confirmado) {
            encomenda.setStatus(Status.NOTIFICACAO_CONFIRMADA);
            encomenda.setDataConfirmacaoNotificacao(LocalDateTime.now());
            encomendaRepository.salvar(encomenda);

            return true;
        }

        return false;
    }

    private boolean enviarNotificacao(Morador morador, Encomenda encomenda) {
        String mensagem = String.format(
                "Olá %s, você recebeu uma encomenda: %s. Por favor, retire na portaria.",
                morador.getNome(),
                encomenda.getDescricao()
        );

        System.out.println("Enviando notificação: " + mensagem);
        return true;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            .withZone(ZoneId.of("America/Sao_Paulo"));
}
