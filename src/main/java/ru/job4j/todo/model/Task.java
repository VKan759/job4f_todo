package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @NonNull
    private String description;

    private ZonedDateTime created = ZonedDateTime.now().truncatedTo(TimeUnit.MINUTES.toChronoUnit());

    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @ManyToMany
    @JoinTable(name = "participates",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private List<Category> categories = new ArrayList<>();
}
