package com.cydeo.converter;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Instead of getting Java object you will get a String from the UI user entry
 * You need to convert this string into ProjectDTO object
 */
@Component
@ConfigurationPropertiesBinding
public class ProjectDtoConverter implements Converter<String, ProjectDTO> {

    ProjectService projectService;

    //injection, @Lazy is added to prevent circular looping of the toString() method calling
    public ProjectDtoConverter(@Lazy ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public ProjectDTO convert(String source) {
        if (source == null || source.equals("")) {
            return null;
        }
        return projectService.getByProjectCode(source);
    }
}
