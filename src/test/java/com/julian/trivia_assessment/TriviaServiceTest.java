package com.julian.trivia_assessment;

import com.julian.trivia_assessment.dto.CheckAnswerRequest;
import com.julian.trivia_assessment.dto.CheckAnswerResponse;
import com.julian.trivia_assessment.dto.QuizResponse;
import com.julian.trivia_assessment.dto.SubmittedAnswer;
import com.julian.trivia_assessment.repository.InMemoryQuizRepository;
import com.julian.trivia_assessment.service.TriviaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.List;

public class TriviaServiceTest {

    private MockRestServiceServer server;
    private TriviaService triviaService;


    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://opentdb.com");
        server = MockRestServiceServer.bindTo(builder).build();
        triviaService = new TriviaService(builder.build(), new InMemoryQuizRepository());
    }

    @Test
    void createQuizReturnsQuestionsIncludingCorrectOne(){
        // Arrange
        String json = """
            {"response_code":0,"results":[{
              "type":"multiple","difficulty":"easy","category":"Animals",
              "question":"How many teeth does an adult rabbit have?","correct_answer":"28",
              "incorrect_answers":["30","26","24"]}]}
            """;
        server.expect(requestTo(containsString("/api.php")))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        // Act
        QuizResponse result = triviaService.createQuiz("easy",1);
        List<String> answers = result.questions().get(0).answers();

        // Assert
        assertEquals(4,answers.size());
        assertTrue(answers.contains("28"));
        assertNotNull(result.quizId());
        assertNotNull(result.questions().get(0).id());
    }

    @Test
    void checkAnswersUnknownQuizIdReturnsNotFound(){
        // Arrange + act
        CheckAnswerRequest request = new CheckAnswerRequest("unknown_id", List.of());

        // Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> triviaService.checkAnswers(request));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void checkAnswersTwoOfThreeCorrectReturnsScoreTwo(){
        // Arrange
        String json = """
            {"response_code":0,"results":[
              {"type":"multiple","difficulty":"easy","category":"Animals",
               "question":"How many teeth does an adult rabbit have?","correct_answer":"28",
               "incorrect_answers":["30","26","24"]},
              {"type":"multiple","difficulty":"easy","category":"Animals",
               "question":"What is the largest mammal in the world?","correct_answer":"Blue whale",
               "incorrect_answers":["African elephant","Giraffe","Polar bear"]},
              {"type":"multiple","difficulty":"easy","category":"Animals",
               "question":"What is the fastest land animal?","correct_answer":"Cheetah",
               "incorrect_answers":["Lion","Greyhound","Pronghorn"]}
            ]}
            """;
        server.expect(requestTo(containsString("/api.php")))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        // Act
        QuizResponse quizResponse = triviaService.createQuiz("easy",3);

        ArrayList<SubmittedAnswer> submittedAnswers = new ArrayList<>();
        submittedAnswers.add(new SubmittedAnswer(quizResponse.questions().get(0).id(),"28"));
        submittedAnswers.add(new SubmittedAnswer(quizResponse.questions().get(1).id(),"Blue whale"));
        submittedAnswers.add(new SubmittedAnswer(quizResponse.questions().get(2).id(),"Lion"));
        CheckAnswerRequest checkAnswerRequest = new CheckAnswerRequest(quizResponse.quizId(), submittedAnswers);
        CheckAnswerResponse checkAnswerResponse = triviaService.checkAnswers(checkAnswerRequest);

        // Assert
        assertEquals(2, checkAnswerResponse.correct());
        assertEquals(3, checkAnswerResponse.total());
    }
}
