import type { Question } from "./Question";

export type Quiz = {
  quizId: string;
  questions: Question[];
};