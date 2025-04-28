package com.techchallenge.encomendas.application.usecases.encomenda;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;

import java.util.List;

public interface BuscarEncomendaUseCase {
    EncomendaDTO buscarPorId(Long id);
    List<EncomendaDTO> buscarPorMorador(Long idMorador);
}
