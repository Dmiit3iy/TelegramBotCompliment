package com.dmiit3iy.telegrambot.controllers;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.Person;
import com.dmiit3iy.telegrambot.services.ComplimentService;
import com.dmiit3iy.telegrambot.services.HistoryService;
import com.dmiit3iy.telegrambot.services.PersonService;
import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendAudio;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

@BotController
public class TelegramBotController implements TelegramMvcController {
    @Value("${bot.token}")
    private String token;

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    private ComplimentService complimentService;

    @Autowired
    public void setComplimentService(ComplimentService complimentService) {
        this.complimentService = complimentService;
    }

    private HistoryService historyService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    private Keyboard replyKeyboardMarkup;

    @PostConstruct
    public void init() {
        this.replyKeyboardMarkup = new ReplyKeyboardMarkup(
                "/next", "/all")
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);

        this.complimentService.addAll();
    }

    @Override
    public String getToken() {
        return this.token;
    }

    private SendMessage sendMessageWithButtons(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage.parseMode(ParseMode.HTML);
    }

    private SendMessage sendMessageListWithButtons(long chatId, List<Compliment> compliments) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Все это про тебя:");
        stringBuilder.append("\n");
        for (Compliment c : compliments) {
            stringBuilder.append(c.getCompliment());
            stringBuilder.append("\n");
        }
        String message = stringBuilder.toString();
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage.parseMode(ParseMode.HTML);
    }


    /**
     * Callback for /start message
     *
     * @param user from bot
     * @param chat from bot
     * @return message to client
     */
    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest start(User user, Chat chat) {

        long chatId = chat.id();
        String firstName = user.firstName();
        String lastName = user.lastName();
        String userName = user.username();
        if (personService.getById(chatId) == null) {
            Person person = new Person();
            person.setChatId(chatId);
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setUserName(userName);
            personService.add(person);
        }
        historyService.add("Нажал кнопку start", chat.id());
        if (firstName != null) {
            return sendMessageWithButtons(chat.id(), "Привет, " + firstName + "! \uD83D\uDD25 Хочешь немного <b>комплиментов</b>?");
        }
        return sendMessageWithButtons(chat.id(), "Привет, " + userName + "!! \uD83D\uDD25 Хочешь немного <b>комплиментов</b>?");
    }

    @BotRequest(value = "/all", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getAll(User user, Chat chat) {
        List<Compliment> list = complimentService.getAll();
        historyService.add("Нажал кнопку all", chat.id());
        return sendMessageListWithButtons(chat.id(), list);
    }
    @Transactional
    @BotRequest(value = "/next", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getNext(User user, Chat chat) {
        Person person = personService.getById(user.id());
        Compliment compliment = complimentService.getNext(person);
        person.addCompliment(compliment);
        personService.update(person);
        historyService.add("Нажал кнопку next", chat.id());

        return sendMessageWithButtons(chat.id(),  "Сегодня ты самый "+ "<b>"+compliment.getCompliment()+"</b> человек");
    }


    /**
     * Callback for other messages
     *
     * @param text from bot
     * @param user from bot
     * @param chat from bot
     * @return message to client
     */
    @BotRequest(value = "{message:[\\S ]+}", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest all(@BotPathVariable("message") String text, User user, Chat chat) {
        return sendMessageWithButtons(chat.id(), "Используйте пожалуйста кнопки /next или /all");
    }
}
