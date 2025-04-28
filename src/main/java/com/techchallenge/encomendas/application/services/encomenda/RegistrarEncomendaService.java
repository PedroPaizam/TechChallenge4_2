package com.techchallenge.encomendas.application.services.encomenda;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.NovaEncomendaDTO;
import com.techchallenge.encomendas.application.mapper.EncomendaMapper;
import com.techchallenge.encomendas.application.usecases.encomenda.RegistrarEncomendaUseCase;
import com.techchallenge.encomendas.domain.entities.Encomenda;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.enums.Status;
import com.techchallenge.encomendas.domain.exceptions.CamposObrigatoriosException;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaJaRetiradaException;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaNaoEncontradaException;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoException;
import com.techchallenge.encomendas.domain.repositories.EncomendaRepository;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import com.techchallenge.encomendas.infrastructure.messaging.MensageriaGateway;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrarEncomendaService implements RegistrarEncomendaUseCase {

    private final EncomendaRepository encomendaRepository;
    private final MoradorRepository moradorRepository;
    private final EncomendaMapper encomendaMapper;
    private final MensageriaGateway mensageriaGateway;

    public RegistrarEncomendaService(EncomendaRepository encomendaRepository,
                                     MoradorRepository moradorRepository,
                                     EncomendaMapper encomendaMapper,
                                     MensageriaGateway mensageriaGateway) {
        this.encomendaRepository = encomendaRepository;
        this.moradorRepository = moradorRepository;
        this.encomendaMapper = encomendaMapper;
        this.mensageriaGateway = mensageriaGateway;
    }

    @Override
    @Transactional
    public EncomendaDTO registrar(NovaEncomendaDTO novaEncomendaDTO) {
        validarCamposObrigatorios(novaEncomendaDTO);

        Morador morador;
        if (novaEncomendaDTO.moradorId() != null) {
            morador = moradorRepository.buscarPorId(novaEncomendaDTO.moradorId())
                    .orElseThrow(() -> new MoradorNaoEncontradoException(novaEncomendaDTO.moradorId()));
        } else if (novaEncomendaDTO.moradorCpf() != null && !novaEncomendaDTO.moradorCpf().isEmpty()) {
            morador = moradorRepository.buscarPorCpf(novaEncomendaDTO.moradorCpf())
                    .orElseThrow(() -> new MoradorNaoEncontradoException(novaEncomendaDTO.moradorCpf()));
        } else {
            throw new CamposObrigatoriosException("É necessário informar o ID ou CPF do morador");
        }

        Encomenda encomenda = new Encomenda();
        encomenda.setMorador(morador);
        encomenda.setDescricao(novaEncomendaDTO.descricao());
        encomenda.setDataRecebimento(LocalDateTime.now());
        encomenda.setStatus(Status.RECEBIDA);
        encomenda.setRecebidaPor(novaEncomendaDTO.recebidaPor());

        Encomenda encomendaSalva = encomendaRepository.salvar(encomenda);

        EncomendaDTO encomendaDTO = encomendaMapper.toDTO(encomendaSalva);

        mensageriaGateway.enviarParaFilaNotificacao(encomendaDTO);

        return encomendaDTO;
    }

    @Override
    @Transactional
    public EncomendaDTO registrarRetirada(Long encomendaId) {
        Encomenda encomenda = encomendaRepository.buscarPorId(encomendaId)
                .orElseThrow(() -> new EncomendaNaoEncontradaException(encomendaId));

        if (Status.RETIRADA.equals(encomenda.getStatus())) {
            throw new EncomendaJaRetiradaException(encomendaId);
        }

        encomenda.setStatus(Status.RETIRADA);
        encomenda.setDataRetirada(LocalDateTime.now());

        Encomenda encomendaAtualizada = encomendaRepository.salvar(encomenda);

        EncomendaDTO encomendaDTO = encomendaMapper.toDTO(encomendaAtualizada);

        mensageriaGateway.enviarConfirmacaoRetirada(encomendaDTO);

        return encomendaDTO;
    }

    @Override
    public boolean confirmarNotificacao(Long encomendaId) {
        Encomenda encomenda = encomendaRepository.buscarPorId(encomendaId)
                .orElseThrow(() -> new EncomendaNaoEncontradaException(encomendaId));

        if (encomenda.getDataConfirmacaoNotificacao() != null) {
            return true;
        }

        encomenda.setDataConfirmacaoNotificacao(LocalDateTime.now());
        encomendaRepository.salvar(encomenda);

        return true;
    }

    private void validarCamposObrigatorios(NovaEncomendaDTO novaEncomendaDTO) {
        if (novaEncomendaDTO.descricao() == null || novaEncomendaDTO.descricao().trim().isEmpty()) {
            throw new CamposObrigatoriosException("A descrição da encomenda é obrigatória");
        }

        if (novaEncomendaDTO.recebidaPor() == null || novaEncomendaDTO.recebidaPor().trim().isEmpty()) {
            throw new CamposObrigatoriosException("É necessário informar quem recebeu a encomenda");
        }

        if (novaEncomendaDTO.moradorId() == null &&
                (novaEncomendaDTO.moradorId() == null || novaEncomendaDTO.moradorCpf().trim().isEmpty())) {
            throw new CamposObrigatoriosException("É necessário informar o ID ou CPF do morador");
        }
    }
}
