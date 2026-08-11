package com.speedtype.controller;

import com.speedtype.dto.ParagraphResponse;
import com.speedtype.dto.TopicResponse;
import com.speedtype.model.Difficulty;
import com.speedtype.service.ParagraphService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/paragraphs")
public class ParagraphController {

    private final ParagraphService paragraphService;

    public ParagraphController(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }

    @GetMapping("/topics")
    public List<TopicResponse> getTopics() {
        return paragraphService.getAllTopics();
    }

    @GetMapping("/random")
    public ParagraphResponse getRandomParagraph(@RequestParam String topic, @RequestParam Difficulty difficulty) {
        return paragraphService.getRandomParagraph(topic, difficulty);
    }
}
