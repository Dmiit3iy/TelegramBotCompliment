package com.dmiit3iy.telegrambot.services;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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



    @Override
    public void sendImage(long chatId, MultipartFile file) {
        String name = file.getOriginalFilename();
        try (BufferedOutputStream bufferedOutputStream =
                     new BufferedOutputStream(new FileOutputStream(name))) {
            bufferedOutputStream.write(file.getBytes());

            TelegramBot bot = new TelegramBot(token);
            bot.execute(new SendPhoto(chatId, new File(name)), new Callback<SendPhoto, SendResponse>() {

                @Override
                public void onResponse(SendPhoto sendPhoto, SendResponse sendResponse) {
                    int messageId = sendResponse.message().messageId();

                    //TODO handle messageId
                    System.out.println(messageId);

                    System.out.println(sendResponse);
                }

                @Override
                public void onFailure(SendPhoto sendPhoto, IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
}
