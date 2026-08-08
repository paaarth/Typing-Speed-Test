package com.speedtype.service;

import com.speedtype.dto.ParagraphResponse;
import com.speedtype.dto.TopicResponse;
import com.speedtype.exception.ApiException;
import com.speedtype.model.Difficulty;
import com.speedtype.model.Paragraph;
import com.speedtype.model.Topic;
import com.speedtype.repository.ParagraphRepository;
import com.speedtype.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final TopicRepository topicRepository;
    private final Random random = new Random();

    public ParagraphService(ParagraphRepository paragraphRepository, TopicRepository topicRepository) {
        this.paragraphRepository = paragraphRepository;
        this.topicRepository = topicRepository;
    }

    /** Public topic list — includes the icon key so the topic picker can render
     *  correctly even for topics an admin added after this app was built. */
    public List<TopicResponse> getAllTopics() {
        return topicRepository.findAll().stream()
                .sorted(Comparator.comparing(Topic::getName))
                .map(this::toTopicResponse)
                .toList();
    }

    public ParagraphResponse getRandomParagraph(String topicName, Difficulty difficulty) {
        List<Paragraph> matches = paragraphRepository.findByTopic_NameAndDifficulty(topicName, difficulty);
        if (matches.isEmpty()) {
            throw new ApiException("No paragraphs found for that topic/difficulty yet", HttpStatus.NOT_FOUND);
        }
        Paragraph chosen = matches.get(random.nextInt(matches.size()));
        return toParagraphResponse(chosen);
    }

    private TopicResponse toTopicResponse(Topic t) {
        TopicResponse response = new TopicResponse();
        response.setId(t.getId());
        response.setName(t.getName());
        response.setIcon(t.getIcon());
        return response;
    }

    private ParagraphResponse toParagraphResponse(Paragraph p) {
        ParagraphResponse response = new ParagraphResponse();
        response.setId(p.getId());
        response.setText(p.getText());
        response.setTopic(p.getTopic().getName());
        response.setDifficulty(p.getDifficulty().name());
        response.setWordCount(p.getWordCount());
        return response;
    }
}
