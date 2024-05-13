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
    public String getAllTextMsg() {
        StringBuilder stringBuilder = new StringBuilder("Выбери название картинки и введи название, что бы получить её:");
        File photos = new File("photos");
        List<File> list = List.of(photos.listFiles());
        for (File file : list) {
            stringBuilder.append("\n");
            stringBuilder.append(file.getName());
        }

        return stringBuilder.toString();
    }

    @Override
    public List<File> getAll() {
        File photos = new File("photos");
        return List.of(photos.listFiles());
    }

    @Override
    public Boolean find(String path) {
        boolean res = false;
        List<File> list = getAll();
        for (File file : list) {
            if (file.getName().equals(path)) {
                res = true;
            }
        }

        return res;
    }
}
