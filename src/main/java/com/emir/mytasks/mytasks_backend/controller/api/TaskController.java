package com.emir.mytasks.mytasks_backend.controller.api;

import com.emir.mytasks.mytasks_backend.dto.TaskRequest;
import com.emir.mytasks.mytasks_backend.entity.Task;
import com.emir.mytasks.mytasks_backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Belirli proje altındaki tüm task'ler
    @GetMapping("/projects/{projectId}/tasks")
    public List<Task> getTasksForProject(Principal principal,
                                         @PathVariable Long projectId) {
        return taskService.getTasksForProject(principal.getName(), projectId);
    }

    // Yeni task oluştur
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Task> createTask(Principal principal,
                                           @PathVariable Long projectId,
                                           @Valid @RequestBody TaskRequest request) {
        Task created = taskService.createTaskForProject(principal.getName(), projectId, request);
        return ResponseEntity.ok(created);
    }

    // Tek bir task'i getir
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Task> getTask(Principal principal,
                                        @PathVariable Long taskId) {
        Task task = taskService.getTaskForUser(principal.getName(), taskId);
        return ResponseEntity.ok(task);
    }

    // Task güncelle (status/priority vs.)
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(Principal principal,
                                           @PathVariable Long taskId,
                                           @Valid @RequestBody TaskRequest request) {
        Task updated = taskService.updateTask(principal.getName(), taskId, request);
        return ResponseEntity.ok(updated);
    }

    // Task sil
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(Principal principal,
                                           @PathVariable Long taskId) {
        taskService.deleteTask(principal.getName(), taskId);
        return ResponseEntity.noContent().build();
    }
}
