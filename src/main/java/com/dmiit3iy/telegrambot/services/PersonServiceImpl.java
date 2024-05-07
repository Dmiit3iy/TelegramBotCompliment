package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.Person;
import com.dmiit3iy.telegrambot.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void add(Person user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Пользователь уже добавлен!");
        }
    }

    @Override
    public Person get(String name) {
        return userRepository.findByUserName(name).orElseThrow(() -> new IllegalArgumentException("Такого пользователя нет"));
    }

    @Override
    public Person getById(long chatID) {
        return userRepository.findByChatId(chatID).orElse(null);
    }

    @Override
    public Person clearCompliments(long chatId) {
        Person person = getById(chatId);
        List<Compliment> emptyList = new ArrayList<>();
        person.setCompliments(emptyList);
        return userRepository.save(person);
    }

    @Override
    public Person update(Person person) {
        Person oldPerson = getById(person.getChatId());
        oldPerson.setLastName(person.getLastName());
        oldPerson.setCompliments(person.getCompliments());
        oldPerson.setUserName(person.getUserName());
        oldPerson.setFirstName(person.getFirstName());
        return userRepository.save(oldPerson);
    }
}
