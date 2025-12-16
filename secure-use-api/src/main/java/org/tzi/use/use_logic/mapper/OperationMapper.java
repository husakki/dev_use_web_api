package org.tzi.use.use_logic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.tzi.use.use_logic.DTO.OperationDTO;
import org.tzi.use.use_logic.entities.OperationNTT;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationMapper {
    OperationDTO toDTO(OperationNTT entity);

    OperationNTT toEntity(OperationDTO dto);
}

