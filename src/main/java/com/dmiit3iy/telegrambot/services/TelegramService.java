package com.dmiit3iy.telegrambot.services;


public interface TelegramService {

    void sendMessage(long chatId, String message);

}
