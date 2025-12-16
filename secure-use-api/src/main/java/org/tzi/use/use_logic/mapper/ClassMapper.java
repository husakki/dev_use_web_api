package org.tzi.use.use_logic.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.tzi.use.use_logic.DTO.ClassDTO;
import org.tzi.use.use_logic.entities.ClassNTT;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClassMapper {
    ClassDTO toDTO(ClassNTT entity);

    ClassNTT toEntity(ClassDTO dto);
}
