package com.dmiit3iy.telegrambot.repository;

import com.dmiit3iy.telegrambot.model.Compliment;
import com.dmiit3iy.telegrambot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ComplimentRepository extends JpaRepository<Compliment,Long> {
    Compliment findByCompliment (String compliment);

}
