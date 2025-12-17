package com.emir.mytasks.mytasks_backend.repository;

import com.emir.mytasks.mytasks_backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Belirli bir kullanıcının projeleri
    List<Project> findByUserId(Long userId);
}
