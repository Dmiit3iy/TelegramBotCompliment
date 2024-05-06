package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Person;
import com.dmiit3iy.telegrambot.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
}
