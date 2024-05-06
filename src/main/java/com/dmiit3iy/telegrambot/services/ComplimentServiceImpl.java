package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.ComplimentList;
import com.dmiit3iy.telegrambot.repository.ComplimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplimentServiceImpl implements ComplimentService {

    private ComplimentRepository complimentRepository;

    @Autowired
    public void setComplimentRepository(ComplimentRepository complimentRepository) {
        this.complimentRepository = complimentRepository;
    }

    private ComplimentList complimentList;

    @Autowired
    public void setComplimentList(ComplimentList complimentList) {
        this.complimentList = complimentList;
    }

    @Override
    public void add(Compliment compliment) {
        try {
            complimentRepository.save(compliment);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Комплимент уже добавлен!");
        }
    }

    @Override
    public void addAll() {
        for (Compliment compliment : complimentList.getList()) {
            if (complimentRepository.findByCompliment(compliment.getCompliment()) == null) {
                complimentRepository.save(compliment);
            }
        }
    }

    @Override
    public Compliment getRandom() {

        return null;
    }

    @Override
    public List<Compliment> getAll() {
        List<Compliment> list = complimentRepository.findAll();
        return list;
    }
}
