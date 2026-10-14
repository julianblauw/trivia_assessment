package com.julian.trivia_assessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Question(
        String id,
        String type,
        String difficulty,
        String category,
        String question,
        String correctAnswer,
        List<String> incorrectAnswers
){}
