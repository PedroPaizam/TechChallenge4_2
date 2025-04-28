package com.techchallenge.encomendas.application.mapper;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.domain.entities.Encomenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MoradorMapper.class})
public interface EncomendaMapper {
    @Mapping(source = "morador", target = "morador")
    EncomendaDTO toDTO(Encomenda encomenda);
    Encomenda toEntity(EncomendaDTO dto);
}
