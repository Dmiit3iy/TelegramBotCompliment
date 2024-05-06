package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.History;
import com.dmiit3iy.telegrambot.model.Person;

public interface HistoryService {
    void add(String event, long personId);
}
