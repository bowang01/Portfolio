package com.portfolio.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.portfolio.service.ProjectService;

@Controller
public class HomeController {

    private final ProjectService projectService;

    public HomeController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("projects", projectService.listAll());
        return "index";
    }

    @GetMapping("/projects/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.get(id));
        return "project";
    }
}
