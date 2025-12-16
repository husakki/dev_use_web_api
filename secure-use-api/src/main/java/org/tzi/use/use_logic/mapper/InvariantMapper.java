package org.tzi.use.use_logic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.tzi.use.use_logic.DTO.InvariantDTO;
import org.tzi.use.use_logic.entities.InvariantNTT;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface InvariantMapper {
    InvariantDTO toDTO(InvariantNTT entity);

    InvariantNTT toEntity(InvariantDTO dto);
}
