package com.dmiit3iy.telegrambot.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@Component
@ConfigurationProperties(prefix = "compliments-settings")
public class ComplimentList {
    private List<Compliment> list;
}
