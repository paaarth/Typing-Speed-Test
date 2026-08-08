package com.speedtype.service;

import com.speedtype.dto.TopicRequest;
import com.speedtype.dto.TopicResponse;
import com.speedtype.exception.ApiException;
import com.speedtype.model.Topic;
import com.speedtype.repository.ParagraphRepository;
import com.speedtype.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class AdminTopicService {

    /** Fixed set of icon keys the frontend actually knows how to render — keeps an
     *  admin from typing an arbitrary string that would silently show no icon. */
    private static final Set<String> VALID_ICONS = Set.of(
            "cpu", "leaf", "trophy", "flask", "flame", "book",
            "briefcase", "landmark", "sparkles", "star", "heart", "music"
    );

    private final TopicRepository topicRepository;
    private final ParagraphRepository paragraphRepository;

    public AdminTopicService(TopicRepository topicRepository, ParagraphRepository paragraphRepository) {
        this.topicRepository = topicRepository;
        this.paragraphRepository = paragraphRepository;
    }

    public static Set<String> getValidIcons() {
        return VALID_ICONS;
    }

    public List<TopicResponse> getAll() {
        return topicRepository.findAll().stream()
                .sorted(Comparator.comparing(Topic::getName))
                .map(this::toResponse)
                .toList();
    }

    public TopicResponse create(TopicRequest request) {
        String name = normalizeName(request.getName());
        validateIcon(request.getIcon());
        if (topicRepository.existsByName(name)) {
            throw new ApiException("A topic named '" + name + "' already exists", HttpStatus.CONFLICT);
        }
        Topic topic = new Topic();
        topic.setName(name);
        topic.setIcon(request.getIcon());
        topicRepository.save(topic);
        return toResponse(topic);
    }

    public TopicResponse update(Long id, TopicRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ApiException("Topic not found", HttpStatus.NOT_FOUND));

        String name = normalizeName(request.getName());
        validateIcon(request.getIcon());
        if (!name.equals(topic.getName()) && topicRepository.existsByName(name)) {
            throw new ApiException("A topic named '" + name + "' already exists", HttpStatus.CONFLICT);
        }

        topic.setName(name);
        topic.setIcon(request.getIcon());
        topicRepository.save(topic);
        return toResponse(topic);
    }

    public void delete(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ApiException("Topic not found", HttpStatus.NOT_FOUND));

        long paragraphCount = paragraphRepository.countByTopic_Id(id);
        if (paragraphCount > 0) {
            throw new ApiException(
                    "Can't delete '" + topic.getName() + "' — " + paragraphCount +
                            " paragraph(s) still use it. Delete or reassign them first.",
                    HttpStatus.CONFLICT
            );
        }
        topicRepository.deleteById(id);
    }

    private String normalizeName(String name) {
        return name.trim().toUpperCase();
    }

    private void validateIcon(String icon) {
        if (icon == null || !VALID_ICONS.contains(icon)) {
            throw new ApiException("Icon must be one of: " + VALID_ICONS, HttpStatus.BAD_REQUEST);
        }
    }

    private TopicResponse toResponse(Topic t) {
        TopicResponse response = new TopicResponse();
        response.setId(t.getId());
        response.setName(t.getName());
        response.setIcon(t.getIcon());
        return response;
    }
}
