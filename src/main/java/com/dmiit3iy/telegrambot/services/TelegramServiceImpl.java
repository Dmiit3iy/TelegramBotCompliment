package com.dmiit3iy.telegrambot.services;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TelegramServiceImpl implements TelegramService {
    @Value("${bot.token}")
    private String token;

    @Override
    public void sendMessage(long chatId, String message) {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new SendMessage(chatId, message),
                new Callback<SendMessage, SendResponse>() {
                    @Override
                    public void onResponse(SendMessage sendMessage,
                                           SendResponse sendResponse) {
                        int messageId = sendResponse.message().messageId();
                        System.out.println(messageId);
                    }

                    @Override
                    public void onFailure(SendMessage sendMessage, IOException e) {
                        System.out.println(sendMessage);
                        e.printStackTrace();
                    }
                });
    }
}
