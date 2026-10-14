package com.julian.trivia_assessment.dto;

import java.util.List;

public record QuizResponse(String quizId, List<QuestionDto> questions) {
}

