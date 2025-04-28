package com.techchallenge.encomendas.application.services.encomenda;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.mapper.EncomendaMapper;
import com.techchallenge.encomendas.application.usecases.encomenda.BuscarEncomendaUseCase;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaNaoEncontradaException;
import com.techchallenge.encomendas.domain.repositories.EncomendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarEncomendaService implements BuscarEncomendaUseCase {

    private final EncomendaRepository encomendaRepository;
    private final EncomendaMapper encomendaMapper;

    public BuscarEncomendaService(EncomendaRepository encomendaRepository,
                                  EncomendaMapper encomendaMapper) {
        this.encomendaRepository = encomendaRepository;
        this.encomendaMapper = encomendaMapper;
    }

    @Override
    public EncomendaDTO buscarPorId(Long id) {
        return encomendaRepository.buscarPorId(id)
                .map(encomendaMapper::toDTO)
                .orElseThrow(() -> new EncomendaNaoEncontradaException(id));
    }

    @Override
    public List<EncomendaDTO> buscarPorMorador(Long idMorador) {
        return encomendaRepository.buscarPorMorador(idMorador).stream()
                .map(encomendaMapper::toDTO)
                .toList();
    }
}
