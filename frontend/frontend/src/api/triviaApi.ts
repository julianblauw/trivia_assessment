import type { CheckAnswerRequest } from "../types/CheckAnswerRequest"
import type { CheckAnswerResponse } from "../types/CheckAnswerResponse"
import type { QuizResponse } from "../types/QuizResponse"

export async function fetchQuiz(difficulty: string, amount: number): Promise<QuizResponse> {
    const response = await fetch(`/questions?difficulty=${difficulty}&amount=${amount}`)
    if (!response.ok) {
        throw new Error(`Could not load quiz (${response.status})`)
    }
    return response.json()
}

export async function checkAnswers(request: CheckAnswerRequest): Promise<CheckAnswerResponse> {
  const response = await fetch('/checkanswers', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(request),
  })
  if (!response.ok) {
    throw new Error(`Could not check answers (${response.status})`)
  }
  return response.json()
}