package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
/**
 * In this MapperUtil class a generic class converting entities into DTO and vice-versa.
 * ModelMapper is used to convert entities into DTOs and vice-versa.
 * Data that come from the database might have extra fields/columns
 * such as Inserted-Date-Time, Updated-Date-Time, etc. and we do not
 * want to display these extra fields/data in the UI. That is why we have two separate folders
 * To inject this class object add the @Component annotation
 * you may create the ModelMapper bean in the runner class
 */
@Component
public class MapperUtil {
    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //generic convert mapper
    public <T> T convert(Object objToBeConverted, T convertedObj) {
        return modelMapper.map(objToBeConverted, (Type) convertedObj.getClass());
    }
}
