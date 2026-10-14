# Trivia Assessment

A trivia quiz web app. The Spring Boot backend fetches questions from the
Open Trivia Database and hides the correct answers from the client. The React
frontend lets the user pick a difficulty and number of questions, answer them
and see the score.

## Running the application

Start the backend first, the frontend proxies its requests to port 8080.

### Backend
From the project root:

    ./mvnw spring-boot:run

The API runs on http://localhost:8080.

### Frontend
In a second terminal:

    cd frontend/frontend
    npm install
    npm run dev

Open http://localhost:5173.

(Note: the frontend folder is accidentally nested as `frontend/frontend`.)
