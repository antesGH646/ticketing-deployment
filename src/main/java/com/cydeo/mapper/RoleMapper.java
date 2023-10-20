package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * In this RoleMapper class Role entities are converted into DTO and vice-versa.
 * ModelMapper is used to convert entities into DTOs and vice-versa.
 * Data that come from the database might have extra fields/columns
 * such as Inserted-Date-Time, Updated-Date-Time, etc. and we do not
 * want to display these extra fields/data in the UI. That is why we have two separate folders
 * To inject this class object add the @Component annotation
 * you may create the ModelMapper bean in the runner class
 */
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
