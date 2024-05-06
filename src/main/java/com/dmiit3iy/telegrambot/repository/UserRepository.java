package com.dmiit3iy.telegrambot.repository;


import com.dmiit3iy.telegrambot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUserName(String userName);
    Optional<Person> findByChatId(long chatId);
}
