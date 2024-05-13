package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Person;
import com.pengrad.telegrambot.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private TelegramService telegramService;

    @Autowired
    public void setTelegramService(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    HistoryService historyService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }


    public String registration(String text, Chat chat) {

        Person person = personService.getById(chat.id());
        if (person.getStep().equals("Login")) {
            if (text != null && !text.isEmpty()) {
                person.setLogin(text);
                historyService.add("Установил логин: " + text, chat.id());
                person.setStep("Name");
                personService.update(person);
                return "Введите имя";
            } else {
                return "Логин должен быть не пустым";
            }
        }
        if (person.getStep().equals("Name")) {
            if (text != null && !text.isEmpty()) {
                person.setName(text);
                person.setStep("Age");
                personService.update(person);
                historyService.add("Установил имя: " + text, chat.id());
                return "Ведите возраст";
            } else {
                return "Имя должно быть не пустым";
            }
        }
        if (person.getStep().equals("Age")) {
            try {
                int age = Integer.parseInt(text);
                if (age > 0 && age < 110) {
                    person.setAge(age);
                    person.setStep("Completed");
                    personService.update(person);
                    historyService.add("Установил возраст: " + age, chat.id());
                    return "Вы успешно зарегистрировались";
                } else {
                    return "Возраст может быть от 1 до 110 лет";
                }
            } catch (NumberFormatException e) {
                return "Возраст должен быть числом";
            }
        }
        return "используйте клавиши next и all";
    }

}
