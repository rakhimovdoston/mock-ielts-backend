package com.search.teacher.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mock_packages")
@Getter
@Setter
public class MockPackages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean active = false;
    private String name;
    private String tariff;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int orders;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Double price;
    private int totalSessions;
    private int speakingSessions;
    private int durationWeeks;
}
