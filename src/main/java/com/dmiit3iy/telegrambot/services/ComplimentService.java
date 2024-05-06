package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.ComplimentList;

import java.util.List;

public interface ComplimentService {
    void add(Compliment compliment);

    void addAll();

    Compliment getRandom();

    List<Compliment> getAll();
}
