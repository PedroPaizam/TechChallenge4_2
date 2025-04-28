package com.techchallenge.encomendas.application.usecases.morador;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;

public interface NotificarMoradorUseCase {
    boolean notificar(EncomendaDTO encomendaDTO);
    boolean confirmarNotificacao(Long encomendaId, boolean confirmado);
}
