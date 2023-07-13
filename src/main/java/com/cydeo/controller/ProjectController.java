package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    ProjectService projectService;
    UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    /**
     * This method displays the Project create form UI page
     * @param model Model
     * @return creat html under the project package
     */
    @GetMapping("/create")
    public String createProject(Model model) {

        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("projects", projectService.listAllProjectDetails());
        model.addAttribute("managers", userService.listAllByRole("manager"));

        return "/project/create";
    }

    /**
     * This method saves the user entry into the UI Project List table
     * @param project ProjectDTO
     * @param bindingResult BindingResult
     * @param model Model
     * @return create html under the project package
     */
    @PostMapping("/create")
    public String insertProject(@Valid @ModelAttribute("project") ProjectDTO project,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            //return projects assigned only to the manager, not all projects
           // model.addAttribute("projects", projectService.listAllProjects());
            model.addAttribute("projects", projectService.listAllProjectDetails());
            model.addAttribute("managers", userService.listAllByRole("manager"));
            return "/project/create";
        }
        projectService.save(project);
        return "redirect:/project/create";
    }

    /**
     * This method soft deletes a project by it code
     * @param projectCode String
     * @return soft deleted record (updated) create html under the project folder
     */
    @GetMapping("/delete/{project-code}")
    public String deleteProject(@PathVariable("project-code") String projectCode) {
        projectService.delete(projectCode);
        return "redirect:/project/create";
    }

    @GetMapping("/complete/{project-code}")
    public String completeProject(@PathVariable("project-code") String projectCode) {
        projectService.complete(projectCode);
        return "redirect:/project/create";
    }

    @GetMapping("/update/{project-code}")
    public String editProject(@PathVariable("project-code") String projectCode,
                              Model model) {
        model.addAttribute("project", projectService.getByProjectCode(projectCode));
        model.addAttribute("projects", projectService.listAllProjectDetails());
        model.addAttribute("managers", userService.listAllByRole("manager"));
        return "/project/update";
    }

    @PostMapping("/update")
    public String updateProject(@Valid @ModelAttribute("project") ProjectDTO project,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projects", projectService.listAllProjectDetails());
            model.addAttribute("managers", userService.listAllByRole("manager"));
            return "/project/update";
        }
        projectService.update(project);
        return "redirect:/project/create";
    }

    /**
     * This method is used to display the Project Status page
     * @param model Model
     * @return Project List of the Project Status menu
     */
    @GetMapping("/manager/project-status")
    public String getProjectByManager(Model model) {
        List<ProjectDTO> projects = projectService.listAllProjectDetails();
        model.addAttribute("projects", projects);
        return "/manager/project-status";
    }
}
