package com.speedtype.controller;

import com.speedtype.dto.ParagraphAdminResponse;
import com.speedtype.dto.ParagraphRequest;
import com.speedtype.service.AdminParagraphService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
