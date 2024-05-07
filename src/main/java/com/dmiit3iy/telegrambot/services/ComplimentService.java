package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.ComplimentList;
import com.dmiit3iy.telegrambot.model.Person;

import java.util.List;

public interface ComplimentService {
    void add(Compliment compliment);

    void addAll();

    Compliment getRandom();
    Compliment getNext(Person person);
    String getNext(long chatId);
    List<Compliment> getAll();

}
