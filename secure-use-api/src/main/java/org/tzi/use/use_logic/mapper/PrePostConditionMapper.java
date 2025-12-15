package org.tzi.use.use_logic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.tzi.use.use_logic.DTO.PrePostConditionDTO;
import org.tzi.use.use_logic.entities.PrePostConditionNTT;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrePostConditionMapper {
    PrePostConditionDTO toDTO(PrePostConditionNTT entity);

    PrePostConditionNTT toEntity(PrePostConditionDTO dto);
}
