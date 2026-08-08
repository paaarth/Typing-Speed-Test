package com.speedtype.service;

import com.speedtype.dto.ParagraphAdminResponse;
import com.speedtype.dto.ParagraphRequest;
import com.speedtype.exception.ApiException;
import com.speedtype.model.Paragraph;
import com.speedtype.model.Topic;
import com.speedtype.repository.ParagraphRepository;
import com.speedtype.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AdminParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final TopicRepository topicRepository;

    public AdminParagraphService(ParagraphRepository paragraphRepository, TopicRepository topicRepository) {
        this.paragraphRepository = paragraphRepository;
        this.topicRepository = topicRepository;
    }

    public List<ParagraphAdminResponse> getAll() {
        return paragraphRepository.findAll().stream()
                .sorted(Comparator.comparing((Paragraph p) -> p.getTopic().getName())
                        .thenComparing(p -> p.getDifficulty().name()))
                .map(this::toResponse)
                .toList();
    }

    public ParagraphAdminResponse create(ParagraphRequest request) {
        Topic topic = findTopic(request.getTopicId());
        Paragraph paragraph = new Paragraph();
        applyRequest(paragraph, request, topic);
        paragraphRepository.save(paragraph);
        return toResponse(paragraph);
    }

    public ParagraphAdminResponse update(Long id, ParagraphRequest request) {
        Paragraph paragraph = paragraphRepository.findById(id)
                .orElseThrow(() -> new ApiException("Paragraph not found", HttpStatus.NOT_FOUND));
        Topic topic = findTopic(request.getTopicId());
        applyRequest(paragraph, request, topic);
        paragraphRepository.save(paragraph);
        return toResponse(paragraph);
    }

    public void delete(Long id) {
        if (!paragraphRepository.existsById(id)) {
            throw new ApiException("Paragraph not found", HttpStatus.NOT_FOUND);
        }
        paragraphRepository.deleteById(id);
    }

    private Topic findTopic(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new ApiException("Topic not found", HttpStatus.NOT_FOUND));
    }

    private void applyRequest(Paragraph paragraph, ParagraphRequest request, Topic topic) {
        paragraph.setText(request.getText());
        paragraph.setTopic(topic);
        paragraph.setDifficulty(request.getDifficulty());
        paragraph.setWordCount(request.getText().trim().split("\\s+").length);
    }

    private ParagraphAdminResponse toResponse(Paragraph p) {
        ParagraphAdminResponse response = new ParagraphAdminResponse();
        response.setId(p.getId());
        response.setText(p.getText());
        response.setTopicId(p.getTopic().getId());
        response.setTopicName(p.getTopic().getName());
        response.setDifficulty(p.getDifficulty().name());
        response.setWordCount(p.getWordCount());
        return response;
    }
}
