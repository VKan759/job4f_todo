package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo_user")
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class User {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String name;

    @NonNull
    @Column(unique = true)
    private String email;

    @ToString.Exclude
    @NonNull
    private String password;

    @NonNull
    @Column(name = "user_zone")
    private String timezone;
}
