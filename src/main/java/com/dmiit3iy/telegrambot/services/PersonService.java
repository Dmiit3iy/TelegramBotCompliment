package com.dmiit3iy.telegrambot.services;


import com.dmiit3iy.telegrambot.model.Person;

public interface PersonService {
    void add(Person user);
    Person get (String name);

    Person getById (long chatID);
}
