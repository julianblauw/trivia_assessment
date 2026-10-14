package com.julian.trivia_assessment;

import com.julian.trivia_assessment.controller.TriviaController;
import com.julian.trivia_assessment.dto.QuestionDto;
import com.julian.trivia_assessment.dto.QuizResponse;
import com.julian.trivia_assessment.service.TriviaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TriviaController.class)
public class TriviaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TriviaService service;

    @Test
    void getQuestionsReturnsNoCorrectAnswerInJson() throws Exception {
        QuizResponse quiz = new QuizResponse("q1", List.of(
                new QuestionDto("1", "multiple", "easy", "Animals",
                        "How many teeth does an adult rabbit have?", List.of("30","26","28","24"))));
        when(service.createQuiz(any(),anyInt())).thenReturn(quiz);
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quizId").value("q1"))
                .andExpect(jsonPath("$.questions[0].answers.length()").value(4))
                .andExpect(jsonPath("$.questions[0].correct_answer").doesNotExist())
                .andExpect(jsonPath("$.questions[0].incorrect_answers").doesNotExist());}
}
