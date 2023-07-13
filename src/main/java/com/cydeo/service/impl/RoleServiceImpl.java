package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    //declare the repositories to call methods that execute certain queries
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.mapperUtil = mapperUtil;
    }


    @Override
    public List<RoleDTO> listAllRoles() {
        /*
        //fetch all the roles and return them
        List<Role>  roleList = roleRepository.findAll();
        //Controller requests RoleDTO object not list of roles or Role objects,
       // roleList.stream().map(p->roleMapper.convertToDTO(p)).collect(Collectors.toList());
        List<RoleDTO> roleDTOList = roleList.stream().map(roleMapper::convertToDTO).collect(Collectors.toList());
        // therefore change the entity into dto using mappers or other ones
        return roleDTOList;
        */
        ////////////////////to make it in one line
       // return roleRepository.findAll().stream().map(roleMapper::convertToDTO).collect(Collectors.toList());
        //use mapper util generic method
        return roleRepository.findAll().stream().map(p -> mapperUtil.convert(p, new RoleDTO())).collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(Long id) {
     //   return roleMapper.convertToDTO(roleRepository.findById(id).get());//returns optional
        //use mapper util generic method
        return mapperUtil.convert(roleRepository.findById(id).get(), new RoleDTO());
    }
}
