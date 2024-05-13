package com.dmiit3iy.telegrambot.controllers;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.Person;
import com.dmiit3iy.telegrambot.services.*;
import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;

@BotController
public class TelegramBotController implements TelegramMvcController {
    @Value("${bot.token}")
    private String token;

    private PersonService personService;

    private ComplimentService complimentService;

    private HistoryService historyService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setComplimentService(ComplimentService complimentService) {
        this.complimentService = complimentService;
    }

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    private RegistrationService registrationService;

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    TelegramService telegramService;

    @Autowired
    public void setTelegramService(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    private Keyboard replyKeyboardMarkup;
    private Keyboard replyKeyboardRegistration;

    @PostConstruct
    public void init() {

        this.replyKeyboardMarkup = new ReplyKeyboardMarkup(
                "/next", "/all")
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);
        this.replyKeyboardRegistration = new ReplyKeyboardMarkup(
                "/register")
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

    private SendMessage sendMessageWithButtonsStart(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(replyKeyboardRegistration);
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
        String userName = user.username();
        if (personService.getById(chatId) == null) {
            Person person = new Person();
            person.setChatId(chatId);
            person.setUserName(userName);
            personService.add(person);
        }
        historyService.add("Нажал кнопку start", chat.id());

        return sendMessageWithButtonsStart(chat.id(), "Привет, " + userName + "!! \uD83D\uDD25 Хочешь немного" +
                " <b>комплиментов</b> &#128571?" + "\n" + "тогда пройди простую регистрацию (нажми кнопку register");
    }

    @BotRequest(value = "/all", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getAll(User user, Chat chat) {
        if (personService.isRegistered(chat.id())) {
            List<Compliment> list = complimentService.getAll();
            historyService.add("Нажал кнопку all", chat.id());
            String message = complimentService.getAllInOneString(list);
            return sendMessageWithButtons(chat.id(), message);
        } else {
            return sendMessageWithButtonsStart(chat.id(), "Необходимо пройди простую регистрацию (нажми кнопку register");
        }
    }

    @BotRequest(value = "/register", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest register(User user, Chat chat, Update update) {
        historyService.add("Нажал кнопку register", chat.id());
        String message ="";
        Person person=personService.getById(chat.id());
        String step = person.getStep();
        if (step==null){
            person.setStep("Login");
            personService.update(person);
            message="Введите свой логин";
        }
        if (step.equals("Login")){
            message="Введите свой логин";
        }
        if (step.equals("Age")){
            message="Введите свой возраст";
        }
        if (step.equals("Name")){
            message="Введите своё имя";
        }

        return sendMessageWithButtons(chat.id(), message);
    }



    @BotRequest(value = "/next", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getNext(User user, Chat chat) {
        if (personService.isRegistered(chat.id())) {
            String message = complimentService.getNext(user.id());
            return sendMessageWithButtons(chat.id(), "Сегодня ты самый " + "<b>" + message + "</b> человек &#129321");
        } else {
            return sendMessageWithButtonsStart(chat.id(), "Необходимо пройди простую регистрацию (нажми кнопку register");
        }
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


        String message=registrationService.registration(text,chat);
        return sendMessageWithButtons(chat.id(), message);
    }
}
