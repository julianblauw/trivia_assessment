export type CheckAnswerRequest = {
  quizId: string;
  submittedAnswers: {
    questionId: string;
    answer: string;
  }[];
};