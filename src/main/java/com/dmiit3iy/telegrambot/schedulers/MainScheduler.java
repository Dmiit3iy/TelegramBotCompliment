package com.dmiit3iy.telegrambot.schedulers;

import com.dmiit3iy.telegrambot.controllers.TelegramBotController;
import com.dmiit3iy.telegrambot.model.Person;
import com.dmiit3iy.telegrambot.services.ComplimentService;
import com.dmiit3iy.telegrambot.services.PersonService;
import com.dmiit3iy.telegrambot.services.TelegramService;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class MainScheduler {
    @Value("${chat.id}")
    private long chatId;

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    private TelegramService telegramService;

    @Autowired
    public void setTelegramService(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    private ComplimentService complimentService;

    @Autowired
    public void setComplimentService(ComplimentService complimentService) {
        this.complimentService = complimentService;
    }

    @Scheduled(fixedRate = 10000)
    public void sendCompliment() {
        String message = complimentService.getNext(chatId);
        telegramService.sendMessage(chatId,message);
    }
}
