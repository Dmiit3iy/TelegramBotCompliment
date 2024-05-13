package com.dmiit3iy.telegrambot.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table
@NoArgsConstructor
public class Compliment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    @NonNull
    private String compliment;
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "persons_compliments", joinColumns = @JoinColumn(name="compliment_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private List<Person> persons = new ArrayList<>();

}
