package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.ComplimentList;
import com.dmiit3iy.telegrambot.model.Person;
import com.dmiit3iy.telegrambot.repository.ComplimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ComplimentServiceImpl implements ComplimentService {

    private ComplimentRepository complimentRepository;

    @Autowired
    public void setComplimentRepository(ComplimentRepository complimentRepository) {
        this.complimentRepository = complimentRepository;
    }

    HistoryService historyService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    private ComplimentList complimentList;

    @Autowired
    public void setComplimentList(ComplimentList complimentList) {
        this.complimentList = complimentList;
    }

    @Override
    public void add(Compliment compliment) {
        try {
            complimentRepository.save(compliment);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Комплимент уже добавлен!");
        }
    }

    @Override
    public void addAll() {
        for (Compliment compliment : complimentList.getList()) {
            try {
                complimentRepository.save(compliment);
            } catch (DataIntegrityViolationException ignored) {
            }
        }
    }

    @Override
    public Compliment getRandom() {
        List<Compliment> compliments = getAll();
        Random random = new Random();
        int x = random.nextInt(compliments.size());
        return compliments.get(x);
    }

    @Override
    public List<Compliment> getAll() {
        List<Compliment> list = complimentRepository.findAll();
        return list;
    }

    @Override
    public String getAllInOneString(List<Compliment> compliments) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Все это про тебя &#129395:");
        stringBuilder.append("\n");
        for (Compliment c : compliments) {
            stringBuilder.append(c.getCompliment());
            stringBuilder.append("\n");
        }
        String message = stringBuilder.toString();
        return message;
    }

    public boolean isFull(Person person) {
        List<Compliment> list = person.getCompliments();
        List<Compliment> allCompliments = getAll();
        if (list == null || list.isEmpty()) {
            return false;
        }
        return allCompliments.size() == list.size();
    }


    @Override
    public Compliment getNext(Person person) {
        List<Compliment> list = person.getCompliments();
        Compliment compliment;
        do {
            compliment = getRandom();
        } while (list.contains(compliment) && (!isFull(person)));
        if (isFull(person)) {
            personService.clearCompliments(person.getChatId());
        }
        return compliment;
    }

    @Override
    public String getNext(long chatId) {
        historyService.add("Нажал кнопку next", chatId);
        Person person = personService.getById(chatId);
        List<Compliment> list = person.getCompliments();
        if (isFull(person)) {
            person = personService.clearCompliments(person.getChatId());
            personService.update(person);
            list = person.getCompliments();
        }
        Compliment compliment;
        if (list.isEmpty()) {
            compliment = getRandom();
        } else {
            {
                do {
                    compliment = getRandom();
                } while (list.contains(compliment));
            }
        }

        person.addCompliment(compliment);
        personService.update(person);

        historyService.add("Получил комплимент " + compliment.getCompliment(), chatId);
        return compliment.getCompliment();
    }


}
