package com.speedtype.controller;

import com.speedtype.dto.TopicRequest;
import com.speedtype.dto.TopicResponse;
import com.speedtype.service.AdminTopicService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** Everything under /api/admin/** requires the ADMIN role — enforced centrally in
 *  SecurityConfig, not per-method here, so there's one place that decides who's an admin. */
@RestController
@RequestMapping("/api/admin/topics")
public class AdminTopicController {

    private final AdminTopicService adminTopicService;

    public AdminTopicController(AdminTopicService adminTopicService) {
        this.adminTopicService = adminTopicService;
    }

    @GetMapping
    public List<TopicResponse> getAll() {
        return adminTopicService.getAll();
    }

    @GetMapping("/icons")
    public Map<String, Object> getValidIcons() {
        return Map.of("icons", AdminTopicService.getValidIcons());
    }

    @PostMapping
    public TopicResponse create(@Valid @RequestBody TopicRequest request) {
        return adminTopicService.create(request);
    }

    @PutMapping("/{id}")
    public TopicResponse update(@PathVariable Long id, @Valid @RequestBody TopicRequest request) {
        return adminTopicService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        adminTopicService.delete(id);
    }
}
