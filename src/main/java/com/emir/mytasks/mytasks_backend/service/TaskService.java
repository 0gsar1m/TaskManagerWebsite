package com.emir.mytasks.mytasks_backend.service;

import com.emir.mytasks.mytasks_backend.dto.TaskRequest;
import com.emir.mytasks.mytasks_backend.entity.Label;
import com.emir.mytasks.mytasks_backend.entity.Project;
import com.emir.mytasks.mytasks_backend.entity.Task;
import com.emir.mytasks.mytasks_backend.entity.User;
import com.emir.mytasks.mytasks_backend.exception.ResourceNotFoundException;
import com.emir.mytasks.mytasks_backend.repository.LabelRepository;
import com.emir.mytasks.mytasks_backend.repository.ProjectRepository;
import com.emir.mytasks.mytasks_backend.repository.TaskRepository;
import com.emir.mytasks.mytasks_backend.repository.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository,
                       LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private Project getProjectOwnedByUser(String username, Long projectId) {
        User user = getUserOrThrow(username);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not own this project");
        }

        return project;
    }

    private Task getTaskOwnedByUser(String username, Long taskId) {
        User user = getUserOrThrow(username);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not own this task");
        }

        return task;
    }

    private Set<Label> resolveLabels(TaskRequest request) {
        Set<Label> labels = new HashSet<>();
        if (request.getLabelIds() != null) {
            for (Long labelId : request.getLabelIds()) {
                Label label = labelRepository.findById(labelId)
                        .orElseThrow(() -> new ResourceNotFoundException("Label not found: " + labelId));
                labels.add(label);
            }
        }
        return labels;
    }

    public List<Task> getTasksForProject(String username, Long projectId) {
        Project project = getProjectOwnedByUser(username, projectId);
        return taskRepository.findByProjectId(project.getId());
    }

    public Task getTaskForUser(String username, Long taskId) {
        return getTaskOwnedByUser(username, taskId);
    }


    public Task createTaskForProject(String username, Long projectId, TaskRequest request) {
        Project project = getProjectOwnedByUser(username, projectId);

        Task task = new Task();
        task.setProject(project);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setLabels(resolveLabels(request));

        return taskRepository.save(task);
    }

    public Task updateTask(String username, Long taskId, TaskRequest request) {
        Task task = getTaskOwnedByUser(username, taskId);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setLabels(resolveLabels(request));

        return taskRepository.save(task);
    }

    public void deleteTask(String username, Long taskId) {
        Task task = getTaskOwnedByUser(username, taskId);
        taskRepository.delete(task);
    }
}
