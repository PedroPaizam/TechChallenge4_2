package com.techchallenge.encomendas.application.mapper;

import com.techchallenge.encomendas.application.dto.NovaEncomendaDTO;
import com.techchallenge.encomendas.domain.entities.Encomenda;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NovaEncomendaMapper {
    Encomenda toEntity(NovaEncomendaDTO dto);
}
