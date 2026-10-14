package com.julian.trivia_assessment.controller;

import com.julian.trivia_assessment.dto.CheckAnswerRequest;
import com.julian.trivia_assessment.dto.CheckAnswerResponse;
import com.julian.trivia_assessment.dto.QuizResponse;
import com.julian.trivia_assessment.service.TriviaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TriviaController {

    private final TriviaService triviaService;
    public TriviaController(TriviaService triviaService){
        this.triviaService = triviaService;
    }

    @GetMapping(path="/questions")
    public ResponseEntity<QuizResponse> createQuiz(
            @RequestParam(required = false, defaultValue = "easy") @Pattern(regexp = "easy|medium|hard") String difficulty,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(50) int amount
    ){
        QuizResponse quizResponse = triviaService.createQuiz(difficulty,amount);
        return ResponseEntity.ok(quizResponse);
    }

    @PostMapping(path="/checkanswers")
    public ResponseEntity<CheckAnswerResponse> checkAnswers(
            @Valid @RequestBody CheckAnswerRequest request
            ){
        CheckAnswerResponse checkAnswerResponse = triviaService.checkAnswers(request);
        return ResponseEntity.ok(checkAnswerResponse);
    }
}
