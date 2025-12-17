package com.emir.mytasks.mytasks_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LabelRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 20)
    private String color;

    public LabelRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
