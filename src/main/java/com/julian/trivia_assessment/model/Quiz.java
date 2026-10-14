package com.julian.trivia_assessment.model;

import java.util.List;

public record Quiz (String id, List<Question> questions){}
