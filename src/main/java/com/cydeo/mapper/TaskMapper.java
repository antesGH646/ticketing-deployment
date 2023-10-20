package com.cydeo.mapper;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * In this TaskMapper class Task entities are converted into DTO and vice-versa.
 * ModelMapper is used to convert entities into DTOs and vice-versa.
 * Data that come from the database might have extra fields/columns
 * such as Inserted-Date-Time, Updated-Date-Time, etc. and we do not
 * want to display these extra fields/data in the UI. That is why we have two separate folders
 * To inject this class object add the @Component annotation
 * you may create the ModelMapper bean in the runner class
 */
@Component
public class TaskMapper {
    private final ModelMapper modelMapper;

    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Task convertToEntity(TaskDTO dto) {
        return modelMapper.map(dto, Task.class);
    }

    public TaskDTO convertToDTO(Task entity) {
        return modelMapper.map(entity, TaskDTO.class);
    }
}
