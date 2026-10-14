package com.julian.trivia_assessment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CheckAnswerRequest(
        @NotBlank String quizId,
        @NotNull List<SubmittedAnswer> submittedAnswers
){}