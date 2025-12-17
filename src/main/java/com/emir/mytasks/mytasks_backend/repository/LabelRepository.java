package com.emir.mytasks.mytasks_backend.repository;

import com.emir.mytasks.mytasks_backend.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {

    Optional<Label> findByName(String name);
    boolean existsByName(String name);
}
