package com.dmiit3iy.telegrambot.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Service
public class PhotoServiceImp implements PhotoService {
    @Override
    public File getFileMime(HttpServletResponse response, String filename) {
        return null;
    }

    @Override
    public String getAll() {
        StringBuilder stringBuilder = new StringBuilder("Выбери название картинки и введи название, что бы получить её:");
        File photos = new File("photos");
        List<File> list = List.of(photos.listFiles());
        for (File file : list) {
            stringBuilder.append("\n");
            stringBuilder.append(file);
        }

        return stringBuilder.toString();
    }
}
