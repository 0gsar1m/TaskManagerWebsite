package com.emir.mytasks.mytasks_backend.service;

import com.emir.mytasks.mytasks_backend.dto.LabelRequest;
import com.emir.mytasks.mytasks_backend.entity.Label;
import com.emir.mytasks.mytasks_backend.exception.ResourceNotFoundException;
import com.emir.mytasks.mytasks_backend.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Label getLabel(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found: " + id));
    }

    public Label createLabel(LabelRequest request) {

        if (labelRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Label name already exists: " + request.getName());
        }

        Label label = new Label();
        label.setName(request.getName());
        label.setColor(request.getColor());

        return labelRepository.save(label);
    }

    public Label updateLabel(Long id, LabelRequest request) {
        Label label = getLabel(id);

        label.setName(request.getName());
        label.setColor(request.getColor());

        return labelRepository.save(label);
    }

    public void deleteLabel(Long id) {
        Label label = getLabel(id);
        labelRepository.delete(label);
    }
}
