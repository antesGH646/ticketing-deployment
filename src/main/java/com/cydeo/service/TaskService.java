package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id); //to find a task by id
    List<TaskDTO> findAllTasks();
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);
    int totalUncompletedTasks(String projectCode);
    int totalCompletedTasks(String projectCode);

    void deleteByProject(ProjectDTO convertToDTO);

    void completeByProject(ProjectDTO convertToDTO);

    List<TaskDTO> listAllTasksByStatusIsNot(Status complete);

    List<TaskDTO> listAllTasksByStatus(Status complete);

    void updateStatus(TaskDTO task);

    List<TaskDTO> listAllTasksByAssignedEmployee(User assignedEmployee);
}
