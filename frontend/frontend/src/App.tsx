import { useState } from 'react';
import './App.css'
import type { MouseEvent } from "react";
import Layout from './components/Layout';
import QuizScreen from './components/QuizScreen';
import StartScreen from './components/StartScreen';
import type { QuizResponse } from './types/QuizResponse';
import { checkAnswers, fetchQuiz } from './api/triviaApi';
import type { CheckAnswerResponse } from './types/CheckAnswerResponse';

function App() {
  const [selectedAmountOfQuestions, setSelectedAmountOfQuestions] = useState(10);
  const [selectedDifficulty, setSelectedDifficulty] = useState("easy");
  const [quiz, setQuiz] = useState<QuizResponse | null>(null);
  const [result, setResult] = useState<CheckAnswerResponse | null>(null)

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function finishQuiz(answers: Record<string, string>) {
    if (!quiz) return
    try {
      setLoading(true)
      setError(null);
      const submittedAnswers = Object.entries(answers).map(
        ([questionId, answer]) => ({ questionId, answer })
      )
      const data = await checkAnswers({ quizId: quiz.quizId, submittedAnswers })
      setResult(data);
    } catch {
      setError('Failed to check your answers.')
    } finally {
      setLoading(false);
    }
  }

  async function handleStartQuiz(){
    try {
      setLoading(true);
      setError(null);
      const data = await fetchQuiz(selectedDifficulty, selectedAmountOfQuestions);
      setQuiz(data);
    } catch {
      setError('Failed to retrieve quiz.')
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout>
      {quiz ? (
        <QuizScreen quiz={quiz} onHome={goHome} onFinish={finishQuiz} result={result} error={error} />
      ) : (
        <StartScreen selectedDifficulty={selectedDifficulty}
          onDifficultyClick={onDifficultyClick}
          selectedAmountOfQuestions={selectedAmountOfQuestions}
          onAmountOfQuestionsClick={onAmountOfQuestionsClick}
          onStartQuiz={handleStartQuiz}
          loading={loading}
          error={error}
        />
      )}
    </Layout>
  )

  function onAmountOfQuestionsClick(e: MouseEvent<HTMLButtonElement>) {
    setSelectedAmountOfQuestions(Number(e.currentTarget.value));
  }

  function onDifficultyClick(e: MouseEvent<HTMLButtonElement>) {
    setSelectedDifficulty(e.currentTarget.value);
  }

  function goHome() {
    setQuiz(null);
    setResult(null);
    setError(null);
  }
}

export default App