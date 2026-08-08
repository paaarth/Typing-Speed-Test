package com.speedtype.controller;

import com.speedtype.dto.ParagraphAdminResponse;
import com.speedtype.dto.ParagraphRequest;
import com.speedtype.model.Difficulty;
import com.speedtype.service.AdminParagraphService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/paragraphs")
public class AdminParagraphController {

    private final AdminParagraphService adminParagraphService;

    public AdminParagraphController(AdminParagraphService adminParagraphService) {
        this.adminParagraphService = adminParagraphService;
    }

    @GetMapping
    public List<ParagraphAdminResponse> getAll() {
        return adminParagraphService.getAll();
    }

    /** {"EASY": [15, 40], "MEDIUM": [30, 65], "HARD": [40, 100]} — [min, max] words.
     *  Lets the admin form show live guidance instead of finding out only on submit. */
    @GetMapping("/word-limits")
    public Map<String, int[]> getWordLimits() {
        Map<String, int[]> limits = new LinkedHashMap<>();
        for (Difficulty d : Difficulty.values()) {
            limits.put(d.name(), AdminParagraphService.getWordLimit(d));
        }
        return limits;
    }

    @PostMapping
    public ParagraphAdminResponse create(@Valid @RequestBody ParagraphRequest request) {
        return adminParagraphService.create(request);
    }

    @PutMapping("/{id}")
    public ParagraphAdminResponse update(@PathVariable Long id, @Valid @RequestBody ParagraphRequest request) {
        return adminParagraphService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        adminParagraphService.delete(id);
    }
}
