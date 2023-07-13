package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component//tells spring to create a bean from this class and return it whenever needed
public class RoleMapper {
    private final ModelMapper modelMapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //convert into entity
    public Role convertToEntity(RoleDTO dto) {
        return modelMapper.map(dto, Role.class);
    }
    //convert into dto
    public RoleDTO convertToDTO(Role entity) {
        return modelMapper.map(entity, RoleDTO.class);
    }
}
