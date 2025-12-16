package org.tzi.use.use_logic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.tzi.use.use_logic.DTO.AssociationDTO;
import org.tzi.use.use_logic.entities.AssociationNTT;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssociationMapper {
    AssociationDTO toDTO(AssociationNTT entity);

    AssociationNTT toEntity(AssociationDTO dto);
}
