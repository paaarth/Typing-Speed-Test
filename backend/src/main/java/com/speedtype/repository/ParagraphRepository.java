package com.speedtype.repository;

import com.speedtype.model.Difficulty;
import com.speedtype.model.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

    List<Paragraph> findByTopic_NameAndDifficulty(String topicName, Difficulty difficulty);

    long countByTopic_Id(Long topicId);
}
