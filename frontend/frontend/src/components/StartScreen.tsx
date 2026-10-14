import type { MouseEvent } from "react";

type StartScreenProps = {
  selectedDifficulty: string
  onDifficultyClick: (e: MouseEvent<HTMLButtonElement>) => void
  selectedAmountOfQuestions: number
  onAmountOfQuestionsClick: (e: MouseEvent<HTMLButtonElement>) => void
  onStartQuiz: () => void;
  loading: boolean;
  error: string | null;
}

function StartScreen({
  selectedDifficulty,
  onDifficultyClick,
  selectedAmountOfQuestions,
  onAmountOfQuestionsClick,
  onStartQuiz,
  loading,
  error,
}: StartScreenProps) {
  const amountOfQuestions = [5, 10, 15, 20];

  const difficulties = [
    { label: "Easy", value: "easy" },
    { label: "Medium", value: "medium" },
    { label: "Hard", value: "hard" },
  ];

  return (
    <div>
      {error && <p className="text-red-500">{error}</p>}
      <h1 className="text-4xl font-bold mb-5">
        Test your knowledge.
      </h1>

      <h2 className="mb-5">
        Challenge yourself with a quick trivia quiz.
      </h2>

      <div className="w-full p-24 shadow-sm max-w-3xl flex flex-col gap-5 bg-white rounded-2xl">
        {/* Difficulty */}
        <div>
          <h3 className="font-extrabold mb-3">Difficulty</h3>

          <div className="bg-[#F2F3FF] py-2 rounded-2xl text-center">
            {difficulties.map((item) => (
              <button
                key={item.value}
                value={item.value}
                className={`px-15 py-2 text-black font-extrabold rounded-2xl transition cursor-pointer ${selectedDifficulty === item.value
                  ? "bg-[#4F46E5] text-white"
                  : ""
                  }`}
                onClick={onDifficultyClick}
              >
                {item.label}
              </button>
            ))}
          </div>
        </div>

        {/* Number of questions */}
        <div>
          <h3 className="font-extrabold mb-3">
            Number of questions
          </h3>

          <div className="flex gap-5">
            {amountOfQuestions.map((amount) => (
              <button
                key={amount}
                value={amount}
                className={`px-10 py-3 font-extrabold border border-black/25 hover:border-black cursor-pointer rounded-2xl duration-200 ${selectedAmountOfQuestions === amount
                  ? "text-[#4F46E5] border-[#4F46E5] bg-[#F2F3FF]"
                  : "text-black"
                  }`}
                onClick={onAmountOfQuestionsClick}
              >
                {amount}
              </button>
            ))}
          </div>
        </div>

        {/* Category */}
        <div>
          <h3 className="font-extrabold">Category</h3>
          <h3>Animals</h3>
        </div>

        {/* Start quiz */}
        <button
          className="bg-[#4F46E5] text-white px-12 py-3 rounded-2xl hover:bg-[#3d32e6] transition hover:cursor-pointer"
          onClick={onStartQuiz}
          disabled={loading}
        >
          {loading ? 'Loading quiz...' : 'Start Quiz'}
        </button>
      </div>
    </div>
  );
}

export default StartScreen;