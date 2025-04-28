package com.techchallenge.encomendas.application.usecases.encomenda;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.NovaEncomendaDTO;

public interface RegistrarEncomendaUseCase {
    EncomendaDTO registrar(NovaEncomendaDTO novaEncomendaDTO);
    EncomendaDTO registrarRetirada(Long encomendaId);
    boolean confirmarNotificacao(Long encomendaId);
}
