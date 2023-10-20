package com.cydeo.converter;

import com.cydeo.dto.RoleDTO;
import com.cydeo.service.RoleService;
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
public class RoleDtoConverter implements Converter<String, RoleDTO> {

    RoleService roleService;

    //injection
    public RoleDtoConverter(@Lazy RoleService roleService) {
        this.roleService = roleService;
    }

     /**
      * Converts the fetched string format id into Long
      * @param source String
      * @return RoleDTO
      */
    @Override
    public RoleDTO convert(String source) {

        if (source == null || source.equals("")) {
            return null;
        }
        return roleService.findById(Long.parseLong(source));
    }
}
