package com.emir.mytasks.mytasks_backend.repository;

import com.emir.mytasks.mytasks_backend.entity.Task;
import com.emir.mytasks.mytasks_backend.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Belirli bir projenin görevleri
    List<Task> findByProjectId(Long projectId);

    // Belirli bir kullanıcının tüm görevleri (projeler üzerinden)
    List<Task> findByProject_User_Id(Long userId);

    // Duruma göre filtre
    List<Task> findByStatus(TaskStatus status);

    // Vakti gelmiş/gelen görevler için örnek filtre
    List<Task> findByDueDateBefore(LocalDate date);
}
