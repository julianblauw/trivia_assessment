import { useState } from "react";
import type { Quiz } from "../types/Quiz";
import type { MouseEvent } from "react";
import type { CheckAnswerResponse } from "../types/CheckAnswerResponse";

type QuizScreenProps = {
    quiz: Quiz;
    onFinish: (submittedAnswers: Record<string, string>) => void
    result: CheckAnswerResponse | null,
    onHome: () => void;
    error: string | null;
}

function QuizScreen({
    quiz,
    onFinish,
    result,
    onHome,
    error
}: QuizScreenProps) {
    const [currentQuestion, setCurrentQuestion] = useState(0);
    const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null);
    const [showAnswerError, setShowAnswerError] = useState(false);
    const [submittedAnswers, setSubmittedAnswers] = useState<Record<string, string>>({})
    const question = quiz.questions[currentQuestion];

    if (result) {
        return (
            <div className="flex flex-col gap-5">
                <h2 className='font-bold text-4xl text-center'>Quiz finished!</h2>
                <h2 className='font-bold text-4xl'>You have {result.correct} correct answer(s) out of {result.total}</h2>
                <button className='text-white py-2 px-4 bg-[#3d32e6] rounded-2xl cursor-pointer hover:bg-[#0d02bf] transition' onClick={onHome}>Return to homescreen</button>
            </div>
        )
    }

    return (
        <div className="flex flex-col gap-5">
            {error && <p className="text-red-500">{error}</p>}
            <h3>Question {currentQuestion + 1} of {quiz.questions.length}</h3>
            <h1 className='font-bold text-4xl'>{question.question}</h1>
            <div className='flex flex-col gap-5 '>
                {question.answers.map(answer => {
                    return (
                        <button className={`p-5 font-bold bg-white rounded-2xl cursor-pointer border-3  hover:text-[#0d02bf] transition ${selectedAnswer === answer ? "bg-[#F2F3FF] text-[#3d32e6] border-[#3d32e6] " : "border-black/25 hover:border-black"}`} key={answer} value={answer} onClick={(e) => handleAnswerClick(e)}>
                            {answer}
                        </button>
                    )
                })}
            </div>
            {showAnswerError &&
                <h3 className="bg-red-400 p-5 text-white font-bold">Please select and answer!</h3>
            }
            <button className='text-white py-2 px-4 bg-[#3d32e6] rounded-2xl cursor-pointer hover:bg-[#0d02bf] transition' onClick={() => {
                if (!selectedAnswer) {
                    setShowAnswerError(true);
                    return;
                }
                const updatedAnswers = {
                    ...submittedAnswers,
                    [question.id]: selectedAnswer,
                }

                setSubmittedAnswers(updatedAnswers)

                if (currentQuestion === quiz.questions.length - 1) {
                    onFinish(updatedAnswers);
                } else {
                    setSelectedAnswer(null);
                    setCurrentQuestion((prev) => prev + 1);
                }
            }}>Next Question</button>
        </div>
    )

    function handleAnswerClick(e: MouseEvent<HTMLButtonElement>) {
        setShowAnswerError(false);
        setSelectedAnswer(e.currentTarget.value);
    }
}

export default QuizScreen;