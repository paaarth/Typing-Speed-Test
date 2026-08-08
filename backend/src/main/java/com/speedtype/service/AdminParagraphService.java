package com.speedtype.service;

import com.speedtype.dto.ParagraphAdminResponse;
import com.speedtype.dto.ParagraphRequest;
import com.speedtype.exception.ApiException;
import com.speedtype.model.Difficulty;
import com.speedtype.model.Paragraph;
import com.speedtype.model.Topic;
import com.speedtype.repository.ParagraphRepository;
import com.speedtype.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminParagraphService {

    /** Word-count guardrails per difficulty, so "EASY" and "HARD" mean something
     *  consistent regardless of who's adding content. Loosely calibrated against
     *  the original 48 seeded paragraphs (EASY ~22-29, MEDIUM ~29-37, HARD ~35-45
     *  words) with headroom on both ends for admin-added variety. */
    private static final Map<Difficulty, int[]> WORD_LIMITS = new EnumMap<>(Difficulty.class);
    static {
        WORD_LIMITS.put(Difficulty.EASY, new int[]{15, 40});
        WORD_LIMITS.put(Difficulty.MEDIUM, new int[]{30, 65});
        WORD_LIMITS.put(Difficulty.HARD, new int[]{40, 100});
    }

    private final ParagraphRepository paragraphRepository;
    private final TopicRepository topicRepository;

    public AdminParagraphService(ParagraphRepository paragraphRepository, TopicRepository topicRepository) {
        this.paragraphRepository = paragraphRepository;
        this.topicRepository = topicRepository;
    }

    public static int[] getWordLimit(Difficulty difficulty) {
        return WORD_LIMITS.get(difficulty);
    }

    public List<ParagraphAdminResponse> getAll() {
        return paragraphRepository.findAll().stream()
                .sorted(Comparator
                        .comparing((Paragraph p) -> p.getTopic() == null ? "" : p.getTopic().getName())
                        .thenComparing(p -> p.getDifficulty().name()))
                .map(this::toResponse)
                .toList();
    }

    public ParagraphAdminResponse create(ParagraphRequest request) {
        Topic topic = findTopic(request.getTopicId());
        validateWordCount(request.getText(), request.getDifficulty());
        Paragraph paragraph = new Paragraph();
        applyRequest(paragraph, request, topic);
        paragraphRepository.save(paragraph);
        return toResponse(paragraph);
    }

    public ParagraphAdminResponse update(Long id, ParagraphRequest request) {
        Paragraph paragraph = paragraphRepository.findById(id)
                .orElseThrow(() -> new ApiException("Paragraph not found", HttpStatus.NOT_FOUND));
        Topic topic = findTopic(request.getTopicId());
        validateWordCount(request.getText(), request.getDifficulty());
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

    private void validateWordCount(String text, Difficulty difficulty) {
        int count = text.trim().split("\\s+").length;
        int[] limit = WORD_LIMITS.get(difficulty);
        if (count < limit[0] || count > limit[1]) {
            throw new ApiException(
                    difficulty.name() + " paragraphs should be " + limit[0] + "-" + limit[1] +
                            " words (this one is " + count + ")",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void applyRequest(Paragraph paragraph, ParagraphRequest request, Topic topic) {
        paragraph.setText(request.getText());
        paragraph.setTopic(topic);
        paragraph.setDifficulty(request.getDifficulty());
        paragraph.setWordCount(request.getText().trim().split("\\s+").length);
    }

    /** Defensive against paragraphs left over from before Topic was a foreign key
     *  (an old text column, migrated by adding topic_id rather than backfilling
     *  it) — such rows show up as "(no topic)" instead of 500ing the whole list,
     *  so an admin can spot and delete them from the UI. */
    private ParagraphAdminResponse toResponse(Paragraph p) {
        ParagraphAdminResponse response = new ParagraphAdminResponse();
        response.setId(p.getId());
        response.setText(p.getText());
        if (p.getTopic() != null) {
            response.setTopicId(p.getTopic().getId());
            response.setTopicName(p.getTopic().getName());
        } else {
            response.setTopicId(null);
            response.setTopicName("(no topic — from before topics existed; delete and re-add)");
        }
        response.setDifficulty(p.getDifficulty().name());
        response.setWordCount(p.getWordCount());
        return response;
    }
}
