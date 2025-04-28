package com.techchallenge.encomendas.application.mapper;

import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.domain.entities.Morador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoradorMapper {
    MoradorDTO toDTO(Morador entity);
    Morador toEntity(MoradorDTO dto);
}
