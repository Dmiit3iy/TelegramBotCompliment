package com.dmiit3iy.telegrambot.services;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public interface PhotoService {
    File getFileMime (HttpServletResponse response, String filename);
    String getAll();
}
