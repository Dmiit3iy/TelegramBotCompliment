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

              //  telegramService.sendMessage(chat.id(), "Введите имя");

                historyService.add("Установил логин: " + text, chat.id());
                person.setStep("Name");
                personService.update(person);
                return "Введите имя";
            } else {
               // telegramService.sendMessage(chat.id(), "Логин должен быть не пустым");
                return "Логин должен быть не пустым";
            }
        }
        if (person.getStep().equals("Name")) {
            if (text != null && !text.isEmpty()) {
                person.setName(text);
                //telegramService.sendMessage(chat.id(), "Введите возраст");
                person.setStep("Age");
                personService.update(person);
                historyService.add("Установил имя: " + text, chat.id());
                return "Ведите возраст";
            } else {
               // telegramService.sendMessage(chat.id(), "Имя должно быть не пустым");
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
                   // telegramService.sendMessage(chat.id(), "Возраст может быть от 1 до 110 лет");
                    return "Возраст может быть от 1 до 110 лет";
                }
            } catch (NumberFormatException e) {
                //telegramService.sendMessage(chat.id(), "Возраст должен быть числом");
                return "Возраст должен быть числом";
            }
        }
        return "используйте клавиши next и all";
    }


//    public String registration(String text, Chat chat) {
//        StringBuilder stringBuilder = new StringBuilder("Используйте пожалуйста кнопки /next или /all &#128586");
//        Person person = personService.getById(chat.id());
//        System.out.println(text);
//        if (text.startsWith("login_")) {
//            String login = text.substring(6);
//            if (login != null && !login.isEmpty()) {
//                person.setLogin(login);
//                personService.update(person);
//                stringBuilder.delete(0, stringBuilder.length());
//                stringBuilder.append("Логин успешно установлен");
//                historyService.add("Установил логин: " + login, chat.id());
//            } else {
//                stringBuilder.delete(0, stringBuilder.length());
//                stringBuilder.append("Логин должен быть не пустым");
//            }
//        }
//        if (text.startsWith("age_")) {
//            try {
//                int age = Integer.parseInt(text.substring(4));
//                if (age > 0 && age < 110) {
//                    person.setAge(age);
//                    personService.update(person);
//                    stringBuilder.delete(0, stringBuilder.length());
//                    stringBuilder.append("Возраст успешно установлен");
//                    historyService.add("Установил возраст: " + age, chat.id());
//                } else {
//                    stringBuilder.delete(0, stringBuilder.length());
//                    stringBuilder.append("Возраст может быть от 1 до 110 лет");
//                }
//
//            } catch (NumberFormatException e) {
//                stringBuilder.delete(0, stringBuilder.length());
//                stringBuilder.append("Возраст должен быть числом");
//            }
//        }
//        if (text.startsWith("name_")) {
//            String name = text.substring(5);
//            if (name != null && !name.isEmpty()) {
//                person.setName(name);
//                personService.update(person);
//                stringBuilder.delete(0, stringBuilder.length());
//                stringBuilder.append("Имя успешно установлено");
//                historyService.add("Установил имя: " + name, chat.id());
//            } else {
//                stringBuilder.delete(0, stringBuilder.length());
//                stringBuilder.append("Имя должно быть не пустым");
//            }
//        }
//        return stringBuilder.toString();
//    }
}
