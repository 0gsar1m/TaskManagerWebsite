package com.emir.mytasks.mytasks_backend.controller.api;

import com.emir.mytasks.mytasks_backend.entity.Label;
import com.emir.mytasks.mytasks_backend.exception.ResourceNotFoundException;
import com.emir.mytasks.mytasks_backend.repository.LabelRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelRepository labelRepository;

    public LabelController(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @GetMapping
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Label> create(@Valid @RequestBody Label request) {
        Label label = new Label();
        label.setName(request.getName());
        label.setColor(request.getColor());
        Label saved = labelRepository.save(label);
        return ResponseEntity.ok(saved);
    }

    // ---- yeni: label güncelle ----
    @PutMapping("/{id}")
    public ResponseEntity<Label> update(@PathVariable Long id,
                                        @Valid @RequestBody Label request) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found: " + id));

        label.setName(request.getName());
        label.setColor(request.getColor());

        Label saved = labelRepository.save(label);
        return ResponseEntity.ok(saved);
    }

    // ---- yeni: label sil ----
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!labelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Label not found: " + id);
        }
        labelRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
