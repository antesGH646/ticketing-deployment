package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * In this ProjectMapper class Project entities are converted into DTO and vice-versa.
 * ModelMapper is used to convert entities into DTOs and vice-versa.
 * Data that come from the database might have extra fields/columns
 * such as Inserted-Date-Time, Updated-Date-Time, etc. and we do not
 * want to display these extra fields/data in the UI. That is why we have two separate folders
 * To inject this class object add the @Component annotation
 * you may create the ModelMapper bean in the runner class
 */
@Component
public class ProjectMapper {
    private final ModelMapper modelMapper;

    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //convert into entity
    public Project convertToEntity(ProjectDTO dto) {
        return modelMapper.map(dto, Project.class);
    }
    //convert into dto
    public ProjectDTO convertToDTO(Project entity) {
        return modelMapper.map(entity, ProjectDTO.class);
    }
}
