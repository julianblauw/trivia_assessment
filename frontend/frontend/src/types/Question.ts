export type Question = {
  id: string;
  type: "multiple";
  difficulty: "easy" | "medium" | "hard";
  category: string;
  question: string;
  answers: string[];
};