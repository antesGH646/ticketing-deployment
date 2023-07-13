package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * This is a generic method for all mappers
 * To inject something two beans are needed and must add
 * the @Component annotation
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
