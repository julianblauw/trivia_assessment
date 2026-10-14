package com.julian.trivia_assessment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TriviaQuestion(
        String type,
        String difficulty,
        String category,
        String question,
        @JsonProperty("correct_answer") String correctAnswer,
        @JsonProperty("incorrect_answers") List<String> incorrectAnswers
) {}