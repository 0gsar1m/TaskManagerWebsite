package com.emir.mytasks.mytasks_backend.controller.api;

import com.emir.mytasks.mytasks_backend.dto.ProjectRequest;
import com.emir.mytasks.mytasks_backend.entity.Project;
import com.emir.mytasks.mytasks_backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getMyProjects(Principal principal) {
        return projectService.getProjectsForUser(principal.getName());
    }

    @PostMapping
    public ResponseEntity<Project> createProject(Principal principal,
                                                 @Valid @RequestBody ProjectRequest request) {
        Project created = projectService.createProject(principal.getName(), request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(Principal principal,
                                              @PathVariable Long id) {
        Project project = projectService.getProjectForUser(principal.getName(), id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(Principal principal,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody ProjectRequest request) {
        Project updated = projectService.updateProject(principal.getName(), id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(Principal principal,
                                              @PathVariable Long id) {
        projectService.deleteProject(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
