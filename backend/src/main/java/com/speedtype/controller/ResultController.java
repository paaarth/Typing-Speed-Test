package com.speedtype.controller;

import com.speedtype.dto.ResultRequest;
import com.speedtype.dto.ResultResponse;
import com.speedtype.dto.StatsResponse;
import com.speedtype.service.ResultService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** All endpoints here require a valid JWT (see SecurityConfig); Spring injects the
 *  authenticated principal automatically, and authentication.getName() is the username. */
@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping
    public ResultResponse submitResult(@Valid @RequestBody ResultRequest request, Authentication authentication) {
        return resultService.saveResult(authentication.getName(), request);
    }

    @GetMapping("/history")
    public List<ResultResponse> getHistory(Authentication authentication) {
        return resultService.getHistory(authentication.getName());
    }

    @GetMapping("/stats")
    public StatsResponse getStats(Authentication authentication) {
        return resultService.getStats(authentication.getName());
    }
}
