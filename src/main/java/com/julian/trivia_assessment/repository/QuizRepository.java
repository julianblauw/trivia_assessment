package com.julian.trivia_assessment.repository;

import com.julian.trivia_assessment.model.Quiz;

import java.util.Optional;

public interface QuizRepository {
    void save(Quiz quiz);
    Optional<Quiz> findById(String id);
}
