package com.julian.trivia_assessment.service;

import com.julian.trivia_assessment.dto.*;
import com.julian.trivia_assessment.model.Quiz;
import com.julian.trivia_assessment.model.Question;
import com.julian.trivia_assessment.dto.TriviaQuestion;
import com.julian.trivia_assessment.dto.TriviaResponse;
import com.julian.trivia_assessment.repository.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Service
public class TriviaService {
    private final RestClient client;
    private final QuizRepository quizRepository;

    public TriviaService(RestClient client, QuizRepository quizRepository){
        this.client = client;
        this.quizRepository = quizRepository;
    }

    private TriviaResponse fetchQuestions(String difficulty,int amount){
        String url = "https://opentdb.com/api.php?category=27&difficulty=" + difficulty + "&type=multiple&amount=" + amount;
        TriviaResponse response;

        try{
          response = client.get()
                    .uri(url)
                    .retrieve()
                    .body(TriviaResponse.class);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Trivia service is unreachable", ex);
        }

        if(response == null){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Trivia service returned an invalid response");
        }

        switch(response.responseCode()){
            case 0 -> {}
            case 5 -> throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit reached");
            default -> throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unexpected response from trivia service");
        }

        if (response.results() == null || response.results().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Trivia service returned no questions");
        }

        return response;
    }

    // Create quiz
    public QuizResponse createQuiz(String difficulty, int amount){
        String quizId = UUID.randomUUID().toString();

        TriviaResponse response = fetchQuestions(difficulty,amount);

        List<Question> questions = new ArrayList<>();
        List<QuestionDto> questionsDto = new ArrayList<>();

        for(TriviaQuestion triviaQuestion: response.results()){
            String id = UUID.randomUUID().toString();
            Question newQuestion = toQuestion(id,triviaQuestion);
            questions.add(newQuestion);
            questionsDto.add(toDto(newQuestion));
        }

        // Save quiz
        Quiz quiz = new Quiz(quizId, questions);
        quizRepository.save(quiz);

        return new QuizResponse(quizId,questionsDto);
    }

    public CheckAnswerResponse checkAnswers(CheckAnswerRequest request) {
        Quiz quiz = quizRepository.findById(request.quizId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        Map<String, Question> questionsById = new HashMap<>();
        for (Question question : quiz.questions()){
            questionsById.put(question.id(),question);
        }

        int correctCount = 0;
        for(SubmittedAnswer submittedAnswer : request.submittedAnswers()){
            Question question = questionsById.get(submittedAnswer.questionId());
            if(question != null && question.correctAnswer().equals(submittedAnswer.answer())){
                correctCount++;
            }
        }

        return new CheckAnswerResponse(correctCount,quiz.questions().size());
    }

    // Merge correct and incorrect answers into one list and shuffle it
    private QuestionDto toDto(Question question){
        ArrayList<String> allAnswers = new ArrayList<>();
        allAnswers.add(question.correctAnswer());
        allAnswers.addAll(question.incorrectAnswers());

        // Shuffle answers
        Collections.shuffle(allAnswers);

        return new QuestionDto(question.id(),question.type(),question.difficulty(),question.category(),question.question(),allAnswers);
    }

    private Question toQuestion(String id, TriviaQuestion triviaQuestion){
        return new Question(
                id,
                triviaQuestion.type(),
                triviaQuestion.difficulty(),
                triviaQuestion.category(),
                decode(triviaQuestion.question()),
                decode(triviaQuestion.correctAnswer()),
                triviaQuestion.incorrectAnswers().stream().map(this::decode).toList()
        );
    }

    private String decode(String text) {
        return HtmlUtils.htmlUnescape(text);
    }
}
