package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.History;
import com.dmiit3iy.telegrambot.model.Person;
import com.dmiit3iy.telegrambot.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {
    private HistoryRepository historyRepository;

    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void add(String event, long personId) {
        Person person = personService.getById(personId);
        History history = new History();
        history.setEvent(event);
        history.setPerson(person);
        historyRepository.save(history);
    }
}
