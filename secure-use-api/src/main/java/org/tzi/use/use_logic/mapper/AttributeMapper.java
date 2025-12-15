package org.tzi.use.use_logic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.tzi.use.use_logic.DTO.AttributeDTO;
import org.tzi.use.use_logic.entities.AttributeNTT;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttributeMapper {
    AttributeDTO toDTO(AttributeNTT entity);

    AttributeNTT toEntity(AttributeDTO dto);
}

