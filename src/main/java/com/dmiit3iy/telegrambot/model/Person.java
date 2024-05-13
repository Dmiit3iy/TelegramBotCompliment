package com.dmiit3iy.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private long chatId;
    private String userName;
    private String login;
    private String name;
    private int age;
    private String step;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "persons_compliments", joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "compliment_id"))
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private List<Compliment> compliments = new ArrayList<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    @JsonIgnore
    @ToString.Exclude
    private List<History> histories = new ArrayList<>();

    public boolean addCompliment(Compliment compliment) {
        return compliments.add(compliment);
    }

}
