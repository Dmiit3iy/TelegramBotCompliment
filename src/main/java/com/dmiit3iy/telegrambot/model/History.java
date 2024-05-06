package com.dmiit3iy.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
@Table
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String event;
    @NonNull
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime localDateTime=LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @JsonIgnore
    private Person person;
}
