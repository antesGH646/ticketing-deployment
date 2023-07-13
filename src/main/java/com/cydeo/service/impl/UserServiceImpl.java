package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional//must add it, when using ddl operations & derived query methods
public class UserServiceImpl implements UserService {

    //declare the repositories to call methods that execute certain queries
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;

    /**
    * Injection through constructor, ProjectService and UserService beans
    * depend on each other, to solve this circular dependency
    * the @Lazy annotation is added. It means wait until I need it
    */
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           @Lazy ProjectService projectService,
                           TaskService taskService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    //this method is need to display all the users on the UI table
    @Override
    public List<UserDTO> listAllUsers() {
        //get all the users
        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return userMapper.convertToDto(user);
    }

    @Override
    public void save(UserDTO userDTO) {
        //UI password entry is not encoded
       // userRepository.save(userMapper.convertToEntity(userDTO));

        //encoding and saving a UI password entry
       userDTO.setEnabled(true);//when creating a user from the UI form, this will enable authorization
       User user = userMapper.convertToEntity(userDTO);
       user.setPassWord(passwordEncoder.encode(user.getPassWord()));
       userRepository.save(user);

    }

    /**
     * When you are updating, if you do not capture and keep the id,
     * it will create a new record but will not update it.
     * To save UI modified objects or DTO objects in the database you need to
     * convert them into entity objects
     * You need to capture the ID of the object and assign it to the converted entity
     * At last save the converted object.
     * @param dto UserDTO object
     * @return UserDTO object to display it again on the UI form. Every UI displayed object
     * is DTO object
     */
    @Override
    public UserDTO update(UserDTO dto) {
        //to get the id of the current user, first capture the dto/user by the username
        User user = userRepository.findByUserName(dto.getUserName());
        //converting the dto object to entity object, otherwise cannot save it in the db
        User dtoConvertedToEntity = userMapper.convertToEntity(dto);
        //setting the id of the captured object, now it will update, but won't duplicate
        dtoConvertedToEntity.setId(user.getId());
        //saving the updated or the converted object user
        userRepository.save(dtoConvertedToEntity);
        //return the dto object capturing it by username
        return findByUserName(dto.getUserName());
    }

    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

    /**
     * This method deletes a user
     * If an assigned employee is deleted, the assigned tasks
     * do not show up in the Pending Tasks (UI), but they
     * show up in the database, so a manager should not be
     * able to delete an assigned employee.
     * @param username String
     */
    @Override
    public void delete(String username) {
        //don't want to delete from the database, only change the flag in the db
        User user = userRepository.findByUserName(username);
        if(canUserBeDeleted(user)) {//1st checking if a manager can delete an assigned user or not
            user.setIsDeleted(true);//the flag is concatenated
            //enabling to create with the deleted username, b/c username is changed
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
        }
        //if a user cannot be deleted, throws an exception
    }

    /**
     * A helper method for the above delete() method,
     * it checks if an employee can be deleted or not.
     * The purpose is a manager should not be able to delete
     * an assigned employee
     */
    private boolean canUserBeDeleted(User user) {
        switch (user.getRole().getDescription()) {
            case "Manager":
                //get all projects of the manager
                List<ProjectDTO> projectDTOList = projectService.listAllByAssignedManager(user);
                //if the manager has no project assigned, can delete an assigned employee
                return projectDTOList.size() == 0;
            case "Employee":
                //get all tasks assigned to an employee
                List<TaskDTO> taskDTOList = taskService.listAllTasksByAssignedEmployee(user);
                return taskDTOList.size() == 0;
            default:
                return true;
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> userList = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }
}
