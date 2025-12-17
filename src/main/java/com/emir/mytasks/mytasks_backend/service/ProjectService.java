package com.emir.mytasks.mytasks_backend.service;

import com.emir.mytasks.mytasks_backend.dto.ProjectRequest;
import com.emir.mytasks.mytasks_backend.entity.Project;
import com.emir.mytasks.mytasks_backend.entity.User;
import com.emir.mytasks.mytasks_backend.exception.ResourceNotFoundException;
import com.emir.mytasks.mytasks_backend.repository.ProjectRepository;
import com.emir.mytasks.mytasks_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    public List<Project> getProjectsForUser(String username) {
        User user = getUserOrThrow(username);
        return projectRepository.findByUserId(user.getId());
    }

    public Project createProject(String username, ProjectRequest request) {
        User user = getUserOrThrow(username);

        Project project = new Project();
        project.setUser(user);
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return projectRepository.save(project);
    }

    public Project getProjectForUser(String username, Long projectId) {
        User user = getUserOrThrow(username);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not own this project");
        }

        return project;
    }

    public Project updateProject(String username, Long projectId, ProjectRequest request) {
        Project project = getProjectForUser(username, projectId);

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return projectRepository.save(project);
    }

    public void deleteProject(String username, Long projectId) {
        Project project = getProjectForUser(username, projectId);
        projectRepository.delete(project);
    }
}
