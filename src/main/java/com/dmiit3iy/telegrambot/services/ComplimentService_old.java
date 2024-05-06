package com.dmiit3iy.telegrambot.services;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.ComplimentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ComplimentService_old {
    private HashMap<Long, ArrayList<Long>> mapCompliments = new HashMap<>();
//    private ComplimentList complimentList;
//
//    @Autowired
//    public void setComplimentList(ComplimentList complimentList) {
//        this.complimentList = complimentList;
//    }
//
//    private PersonService userService;
//
//    @Autowired
//    public void setUserService(PersonService userService) {
//        this.userService = userService;
//    }
//
//
//    /**
//     * Метод для накопления полученных комплиментов
//     *
//     * @param idUser,idCompliment
//     */
//    public void accountingCompliments(long idUser, long idCompliment) {
//        ArrayList<Long> complimentIdList = mapCompliments.getOrDefault(idUser, new ArrayList<>());
//        complimentIdList.add(idCompliment);
//        mapCompliments.put(idUser, complimentIdList);
//    }
//
//    /**
//     * Метод для проверки повторных комплиментов
//     *
//     * @param idUser,idCompliment
//     * @return
//     */
//    public boolean isRepeatCompliment(long idUser, long idCompliment) {
//        if (mapCompliments.get(idUser) == null) {
//            return false;
//        }
//        return mapCompliments.get(idUser).contains(idCompliment);
//    }
//
//    /**
//     * Проверка на то что все комплименты перечисленны
//     * @param id
//     * @return
//     */
//    public boolean isFull(long id) {
//        if (mapCompliments.get(id) == null) {
//            return false;
//        }
//        return complimentList.getList().size() == mapCompliments.get(id).size();
//    }
//
//    public ComplimentList getComplimentList() {
//        return complimentList;
//    }
//
//    public Compliment getNext(long id){
//        Compliment compliment;
//        do {
//            compliment = this.complimentList.getRandom();
//        }
//        while ((!this.isFull(id)) && this.isRepeatCompliment(id, compliment.getId()));
//
//        if (this.isFull(id)) {
//            this.mapCompliments.get(id).clear();
//        }
//        this.accountingCompliments(id, compliment.getId());
//        return compliment;
//    }
}
