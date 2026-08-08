package com.speedtype.repository;

import com.speedtype.model.TypingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypingResultRepository extends JpaRepository<TypingResult, Long> {

    List<TypingResult> findByUserIdOrderByTestDateAsc(Long userId);

    long countByUserId(Long userId);

    @Query("SELECT AVG(t.wpm) FROM TypingResult t WHERE t.user.id = :userId")
    Double findAverageWpmByUserId(Long userId);

    @Query("SELECT MAX(t.wpm) FROM TypingResult t WHERE t.user.id = :userId")
    Double findMaxWpmByUserId(Long userId);

    @Query("SELECT AVG(t.accuracy) FROM TypingResult t WHERE t.user.id = :userId")
    Double findAverageAccuracyByUserId(Long userId);

    @Query("SELECT MAX(t.wpm) FROM TypingResult t WHERE t.user.id = :userId AND t.difficulty = com.speedtype.model.Difficulty.EASY")
    Double findMaxWpmEasyByUserId(Long userId);

    @Query("SELECT MAX(t.wpm) FROM TypingResult t WHERE t.user.id = :userId AND t.difficulty = com.speedtype.model.Difficulty.MEDIUM")
    Double findMaxWpmMediumByUserId(Long userId);

    @Query("SELECT MAX(t.wpm) FROM TypingResult t WHERE t.user.id = :userId AND t.difficulty = com.speedtype.model.Difficulty.HARD")
    Double findMaxWpmHardByUserId(Long userId);
}
