package com.julian.trivia_assessment.repository;

import com.julian.trivia_assessment.model.Quiz;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryQuizRepository implements QuizRepository{
    private final Map<String, Quiz> quizzes = new ConcurrentHashMap<>();

    @Override
    public void save(Quiz quiz){
        quizzes.put(quiz.id(), quiz);
    }

    @Override
    public Optional<Quiz> findById(String id){
        return Optional.ofNullable(quizzes.get(id));
    }

}