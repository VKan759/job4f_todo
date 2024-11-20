package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class Task {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @EqualsAndHashCode.Include
    private String title;

    @Column(name = "description")
    private String description;

    private LocalDateTime created = LocalDateTime.now().truncatedTo(TimeUnit.MINUTES.toChronoUnit());

    private boolean done;
}
