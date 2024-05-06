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
        if (firstName != null) {
            return sendMessageWithButtons(chat.id(), "Hello, " + firstName + "! \uD83D\uDD25 Welcome to <b>my</b> bot!");
        }
        return sendMessageWithButtons(chat.id(), "Hello, " + userName + "!! \uD83D\uDD25 Welcome to <b>my</b> bot!");
    }

    @BotRequest(value = "/all", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getAll(User user, Chat chat) {
        List<Compliment> list = complimentService.getAll();
        historyService.add("Нажал кнопку all",chat.id());
        return sendMessageListWithButtons(chat.id(), list);
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
        return sendMessageWithButtons(chat.id(), "Thank you for message, you message: " + text);
    }
}
