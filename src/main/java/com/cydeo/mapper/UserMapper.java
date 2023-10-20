package com.cydeo.mapper;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * In this UserMapper class User entities are converted into DTO and vice-versa.
 * ModelMapper is used to convert entities into DTOs and vice-versa.
 * Data that come from the database might have extra fields/columns
 * such as Inserted-Date-Time, Updated-Date-Time, etc. and we do not
 * want to display these extra fields/data in the UI. That is why we have two separate folders
 * To inject this class object add the @Component annotation
 * you may create the ModelMapper bean in the runner class
 */
@Component//tells spring to create a bean from this class and return it whenever needed
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertToEntity(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserDTO convertToDto(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }
}
