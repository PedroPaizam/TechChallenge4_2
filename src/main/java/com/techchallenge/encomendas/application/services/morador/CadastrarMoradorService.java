package com.techchallenge.encomendas.application.services.morador;

import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.mapper.MoradorMapper;
import com.techchallenge.encomendas.application.usecases.morador.CadastrarMoradorUseCase;
import com.techchallenge.encomendas.domain.entities.Morador;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorJaCadastradoException;
import com.techchallenge.encomendas.domain.repositories.MoradorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CadastrarMoradorService implements CadastrarMoradorUseCase {

    private final MoradorRepository moradorRepository;
    private final MoradorMapper moradorMapper;

    public CadastrarMoradorService(MoradorRepository moradorRepository,
                                   MoradorMapper moradorMapper) {
        this.moradorRepository = moradorRepository;
        this.moradorMapper = moradorMapper;
    }

    @Override
    @Transactional
    public MoradorDTO cadastrar(MoradorDTO moradorDTO) {
        if (moradorDTO.cpf() != null && moradorRepository.existePorCpf(moradorDTO.cpf())) {
            throw new MoradorJaCadastradoException(moradorDTO.cpf());
        }

        validarCamposObrigatorios(moradorDTO);

        Morador morador = moradorMapper.toEntity(moradorDTO);

        Morador moradorSalvo = moradorRepository.salvar(morador);

        return moradorMapper.toDTO(moradorSalvo);
    }

    private void validarCamposObrigatorios(MoradorDTO moradorDTO) {
        if (moradorDTO.nome() == null || moradorDTO.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do morador é obrigatório");
        }

        if (moradorDTO.apartamento() == null || moradorDTO.apartamento().trim().isEmpty()) {
            throw new IllegalArgumentException("O número do apartamento é obrigatório");
        }

        if (moradorDTO.telefone() == null || moradorDTO.telefone().trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone do morador é obrigatório");
        }
    }
}
