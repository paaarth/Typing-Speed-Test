package com.speedtype.dto;

import com.speedtype.model.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ParagraphRequest {

    @NotBlank(message = "Text is required")
    @Size(min = 20, max = 2000, message = "Paragraph text should be 20-2000 characters")
    private String text;

    @NotNull(message = "Topic is required")
    private Long topicId;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
