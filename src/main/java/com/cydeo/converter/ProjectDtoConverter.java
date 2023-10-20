package com.cydeo.converter;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * IDs are extracted as string formats from the database,
 * therefore, they are converted into the required data type
 * in order to use them in the DTOs. To do so implement the
 * Converter interface to override the convert() method.
 */
@Component
@ConfigurationPropertiesBinding
public class ProjectDtoConverter implements Converter<String, ProjectDTO> {

    ProjectService projectService;

    //injection, @Lazy is added to prevent circular looping of the toString() method calling
    public ProjectDtoConverter(@Lazy ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Converts the fetched string format id into Long
     * @param source String
     * @return ProjectDTO
     */
    @Override
    public ProjectDTO convert(String source) {
        if (source == null || source.equals("")) {
            return null;
        }
        return projectService.getByProjectCode(source);
    }
}
