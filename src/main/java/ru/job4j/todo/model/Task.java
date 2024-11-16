package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String description;

    @Column(name = "full_description")
    private String fullDescription;

    private LocalDateTime created = LocalDateTime.now().truncatedTo(TimeUnit.MINUTES.toChronoUnit());

    private boolean done;
}
