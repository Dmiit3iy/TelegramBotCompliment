package com.dmiit3iy.telegrambot.services;


import org.springframework.web.multipart.MultipartFile;

public interface TelegramService {

    void sendMessage(long chatId, String message);
    void sendImage(long chatId, MultipartFile file);

}
