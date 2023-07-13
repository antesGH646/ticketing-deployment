package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.TaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;


    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper,
                           ProjectMapper projectMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }

    /**
     * This method finds a task from the db by its id
     * Without first finding the task you cannot update the task.
     * First the fetched task must be converted into DTO object
     * Since findById() method return Optional must make it Optional
     * If the task is found convert it to DTO object and return it
     * otherwise throw make it to throw exception or return null
     * @param id Long
     * @return a task
     */
    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()) {
            return taskMapper.convertToDTO(task.get());
        }
        return null;
    }

    /**
     * This method return a list of tasks to show up in the UI
     * First get all the tasks from db
     * Second convert it to DTO then display it
     * @return a list of tasks
     */
    @Override
    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * This method an assigned task from the UI into the db
     * First convert the UI fetched data(dto objects) into entity object
     * Second save it into the database through the repository
     * Third set the task status to open, and to current date
     * because the status object does not come from the UI form
     * Make sure to declare id in the TaskDTO and the ProjectDTO
     * (private Long id), otherwise it will not save a task
     * with unmatched task or project
     * JpaRepository provides save() method or can create it in the services.
     * @param dto TaskDTO
     */
    @Override
    public void save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(dto);
        taskRepository.save(task);
    }

    /**
     * This make update a task in the db. Since there is no status in the form
     * assign it to a new status. The task may get a status from somewhere
     * therefore check it if is null or not before you assign it.
     * First you need to convert the updated data into entity object
     * Second store in the db through the repository
     * Need to capture the status and the date and set or assign the updated task
     * @param dto TaskDTO
     */
    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask = taskMapper.convertToEntity(dto);
        if(task.isPresent()) {
            convertedTask.setId(task.get().getId());//to avoid duplicated record
            //there is no status inside the form so assign the status, if it is null(may get it from somewhere)
            convertedTask.setTaskStatus(dto.getTaskStatus() == null ? task.get().getTaskStatus() : dto.getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());//assign the date
            taskRepository.save(convertedTask);//save the updated task
        }
    }

    /**
     * This method soft deletes a task
     * First find the task from the db
     *
     * @param id Long
     */
    @Override
    public void delete(Long id) {
        //Since findById() returns Optional, the Task object must be Optional
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent()) {
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public int totalUncompletedTasks(String projectCode) {
        return taskRepository.totalUncompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTasks(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    /**
     * This method is used to delete related tasks after a project is
     * marked deleted.
     * @param project ProjectDTO
     */
    @Override
    public void deleteByProject(ProjectDTO project) {
      List<TaskDTO> taskList = listAllProjects(project);
      taskList.forEach(p -> delete(p.getId()));
    }

    /**
     * This project marks if all assigned tasks are completed or not
     * @param project ProjectDTO
     */
    @Override
    public void completeByProject(ProjectDTO project) {
        //get all the lists
        List<TaskDTO> taskList = listAllProjects(project);
        //get all the tasks and set them to complete, then save the change in the db
        taskList.forEach(p -> {
            p.setTaskStatus(Status.COMPLETE);
            update(p);
        });
    }

    /**
     * This method lists all tasks by status
     * @param status Status
     * @return lis ot TaskDTOs
     */
    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        //No security, a user is hard coded, to see if the user is able to see other's tasks or not
        //User loggedInUser = userRepository.findByUserName("john@employee.com");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findByUserName(username);
        //get all the tasks
        List<Task> taskList = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,loggedInUser);
        return taskList.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    /**
     * This method return a list of tasks by the status
     * @param status Status
     * @return list of tasks
     */
    @Override
    public  List<TaskDTO> listAllTasksByStatus(Status status) {
        //No security, a user is hard coded, to see if the user is able to see other's tasks or not
       // User loggedInUser = userRepository.findByUserName("john@employee.com");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findByUserName(username);
        //get all the tasks
        List<Task> taskList = taskRepository.findAllByTaskStatusAndAssignedEmployee(status,loggedInUser);
        return taskList.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    /**
     * This method update the status only
     * First get the object from the db, it already has the id
     * Second update the task
     * @param dto TaskDTO
     */
    @Override
    public void updateStatus(TaskDTO dto) {
        //id is coming from db not from the dto or UI form, so no need to convert it to entity
        Optional<Task> task = taskRepository.findById(dto.getId());
        if(task.isPresent()) {
            //do the changes on the db directly, set to whatever the status is currently
            task.get().setTaskStatus(dto.getTaskStatus());
            taskRepository.save(task.get()); //save the changes
        }
    }

    @Override
    public List<TaskDTO> listAllTasksByAssignedEmployee(User assignedEmployee) {
        //get a list of tasks assigned to an employee
        List<Task> taskList = taskRepository.findAllByAssignedEmployee(assignedEmployee);
        return taskList.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    /**
     * This is a helper method for the above deleteByProject()
      * and completeByProject() methods
     * @param project ProjectDTO
     * @return list of tasks converted to DTOs
     */
    private List<TaskDTO> listAllProjects(ProjectDTO project) {
        List<Task> taskList = taskRepository.findAllByProject(projectMapper.convertToEntity(project));
        return taskList.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }
}
