package com.speedtype.service;

import com.speedtype.dto.ResultRequest;
import com.speedtype.dto.ResultResponse;
import com.speedtype.dto.StatsResponse;
import com.speedtype.exception.ApiException;
import com.speedtype.model.Paragraph;
import com.speedtype.model.TypingResult;
import com.speedtype.model.User;
import com.speedtype.repository.ParagraphRepository;
import com.speedtype.repository.TypingResultRepository;
import com.speedtype.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ResultService {

    private final TypingResultRepository resultRepository;
    private final UserRepository userRepository;
    private final ParagraphRepository paragraphRepository;

    public ResultService(TypingResultRepository resultRepository, UserRepository userRepository,
                          ParagraphRepository paragraphRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.paragraphRepository = paragraphRepository;
    }

    public ResultResponse saveResult(String username, ResultRequest request) {
        User user = getUser(username);
        Paragraph paragraph = paragraphRepository.findById(request.getParagraphId())
                .orElseThrow(() -> new ApiException("Paragraph not found", HttpStatus.NOT_FOUND));

        TypingResult result = new TypingResult();
        result.setUser(user);
        result.setParagraph(paragraph);
        result.setWpm(round1(request.getWpm()));
        result.setAccuracy(round1(request.getAccuracy()));
        result.setErrors(request.getErrors());
        result.setTimeTakenSeconds(request.getTimeTakenSeconds());
        result.setDifficulty(paragraph.getDifficulty());
        result.setTopic(paragraph.getTopic().getName());
        result.setTestDate(LocalDateTime.now());

        resultRepository.save(result);
        return toResponse(result);
    }

    /** Full history, oldest first, so the profile graph reads left-to-right as a timeline. */
    public List<ResultResponse> getHistory(String username) {
        User user = getUser(username);
        return resultRepository.findByUserIdOrderByTestDateAsc(user.getId())
                .stream()
                .sorted(Comparator.comparing(TypingResult::getTestDate).reversed())
                .map(this::toResponse)
                .toList();
    }

    public StatsResponse getStats(String username) {
        User user = getUser(username);
        Long userId = user.getId();

        Double avgWpm = resultRepository.findAverageWpmByUserId(userId);
        Double maxWpm = resultRepository.findMaxWpmByUserId(userId);
        Double avgAccuracy = resultRepository.findAverageAccuracyByUserId(userId);

        StatsResponse stats = new StatsResponse();
        stats.setAverageWpm(avgWpm == null ? 0 : round1(avgWpm));
        stats.setBestWpm(maxWpm == null ? 0 : maxWpm);
        stats.setAverageAccuracy(avgAccuracy == null ? 0 : round1(avgAccuracy));
        stats.setTotalTests(resultRepository.countByUserId(userId));
        stats.setBestWpmEasy(resultRepository.findMaxWpmEasyByUserId(userId));
        stats.setBestWpmMedium(resultRepository.findMaxWpmMediumByUserId(userId));
        stats.setBestWpmHard(resultRepository.findMaxWpmHardByUserId(userId));
        return stats;
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    private double round1(double value) {
        return Math.round(value * 10) / 10.0;
    }

    private ResultResponse toResponse(TypingResult r) {
        ResultResponse response = new ResultResponse();
        response.setId(r.getId());
        response.setWpm(r.getWpm());
        response.setAccuracy(r.getAccuracy());
        response.setErrors(r.getErrors());
        response.setTimeTakenSeconds(r.getTimeTakenSeconds());
        response.setDifficulty(r.getDifficulty().name());
        response.setTopic(r.getTopic());
        response.setTestDate(r.getTestDate());
        return response;
    }
}
