package com.emir.mytasks.mytasks_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "labels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 20)
    private String color; // "#ff0000" veya "red" gibi

    public Label() {
    }

    // getters/setters
}
