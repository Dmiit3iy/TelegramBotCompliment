package com.dmiit3iy.telegrambot.repository;

import com.dmiit3iy.telegrambot.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Long> {
}
