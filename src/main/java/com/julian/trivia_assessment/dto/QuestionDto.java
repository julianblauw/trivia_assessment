package com.julian.trivia_assessment.dto;

import java.util.List;
    public record QuestionDto(
            String id,
            String type,
            String difficulty,
            String category,
            String question,
           List<String> answers
    ){};