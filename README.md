# Fitness Monolith – Backend

**An AI-powered fitness tracking platform built with Java Spring Boot.**

Fitness Monolith is a full-stack fitness tracking application that helps users record their workouts, monitor activity metrics, and receive AI-generated recommendations based on their activities.

This repository contains the backend application, built using Spring Boot, Spring Security, Spring Data JPA, and PostgreSQL. It exposes REST APIs consumed by the Next.js frontend and integrates with AI inference providers to generate personalised fitness recommendations.

---

## 🚀 Features

### 1. Authentication & Security
- User registration and login.
- JWT-based authentication.
- Spring Security integration for securing REST APIs.
- Password hashing and secure credential handling.
- Protected endpoints for authenticated users.
- User-specific access to activities, recommendations, and profile information.

### 2. Activity Management
Users can record, manage, and track their fitness activities.

Supported activity types:

- Running
- Walking
- Cycling
- Swimming
- Weight Training
- Yoga
- HIIT
- Cardio
- Stretching
- Other

Each activity can include:
- Activity type
- Duration
- Calories burned
- Start and end time
- Additional activity-specific metrics

The additional metrics field allows activity-specific information to be stored flexibly. For example, a weight-training activity can include exercises, sets, and repetitions.

### 3. AI-Powered Recommendations

One of the key features of Fitness Monolith is its AI-powered recommendation engine.

The backend processes logged activity information and generates recommendations using an external or local Large Language Model (LLM).

Recommendations include:

- **Recommendation:** An overall assessment based on the logged activity.
- **Improvements:** Areas where the user can improve their performance.
- **Suggestions:** Actionable suggestions to help improve future workouts.
- **Safety Insights:** Safety-related guidance with a severity level and accompanying message.

The generated recommendation follows a structured JSON response, making it easier for the frontend to display the information consistently.

Example response structure:

```json
{
  "type": "PERFORMANCE",
  "recommendation": "Your workout shows consistent progress.",
  "improvements": [
    "Focus on maintaining a consistent pace."
  ],
  "suggestions": [
    "Gradually increase workout intensity."
  ],
  "safety": {
    "level": "LOW",
    "message": "Maintain proper form and allow sufficient recovery."
  }
}
```

The recommendation type is stored as a string, allowing flexibility when introducing additional recommendation categories.

### 4. Hybrid AI Integration

Fitness Monolith supports both local and cloud-based AI inference.

Instead of tightly coupling the application to a single AI provider, the backend uses environment-based configuration to select the inference provider.

| Environment | Provider | Model |
|---|---|---|
| Local development | Ollama | Qwen3 1.7B |
| Production | Groq API | GPT-OSS 20B |

**Local AI – Ollama**
- Runs locally using Ollama.
- Uses the Qwen3 1.7B model.
- Generates structured recommendation responses using JSON schema formatting.
- Allows experimentation with local LLM inference without depending on a paid cloud inference service.

**Cloud AI – Groq**
- Uses Groq's OpenAI-compatible Chat Completions API.
- Supports cloud-based inference without hosting an LLM on the application server.
- Uses bearer-token authentication.
- Processes structured recommendation prompts and parses the generated JSON response.

The provider, model, base URL, and API key can be configured through environment variables without changing the recommendation-generation workflow.

### 5. Recommendation Management
- Generates recommendations based on logged activities.
- Supports multiple recommendations associated with an activity.
- Stores generated recommendation data in the database.
- Maintains recommendation history.
- Preserves structured improvement areas, suggestions, and safety information.

### 6. User Profile Management
- User profile information management.
- Authenticated access to user-specific information.
- Profile updates through secured backend APIs.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming language |
| Spring Boot | Application framework |
| Spring Security | Authentication and API security |
| Spring Data JPA | Database persistence |
| Hibernate | ORM |
| PostgreSQL | Relational database |
| JWT (JJWT) | Token-based authentication |
| Jakarta Bean Validation | Request validation |
| Jackson | JSON serialization and deserialization |
| Spring RestClient | Communication with AI providers |
| Ollama | Local LLM inference |
| Groq API | Cloud LLM inference |
| Maven | Dependency management and build automation |
| Docker | Containerization |
| Springdoc OpenAPI | API documentation |

---

## 🏗️ Backend Architecture

The backend follows a layered architecture to keep business logic, API handling, persistence, and configuration separated.

```text
Client (Next.js Frontend)
          |
          | HTTP / REST APIs
          v
+-----------------------------+
|       Spring Security       |
|      JWT Authentication    |
+-----------------------------+
          |
          v
+-----------------------------+
|         Controllers        |
|   Handles incoming requests |
+-----------------------------+
          |
          v
+-----------------------------+
|          Services          |
|      Business Logic        |
|                             |
|  - User Service            |
|  - Activity Service        |
|  - Recommendation Service  |
|  - AI Integration Service  |
+-----------------------------+
          |
          v
+-----------------------------+
|        Repositories        |
|       Spring Data JPA      |
+-----------------------------+
          |
          v
+-----------------------------+
|        PostgreSQL          |
|       Neon Database        |
+-----------------------------+

       AI Integration
             |
       +-----+-----+
       |           |
       v           v
     Ollama       Groq
     (Local)     (Cloud)
```

### Application organization

The project uses separate packages for application responsibilities, including:

- `controller` – REST API request handling.
- `service` – Business logic and application workflows.
- `repository` – Database access through Spring Data JPA.
- `modal` – Persistent domain entities.
- `dto` – Request and response data transfer objects.
- `configuration` – Security, AI schema, and application configuration.
- `exception` – Application exception handling.

---

## 🗄️ Database

Fitness Monolith uses **PostgreSQL** as its relational database.

The application uses Spring Data JPA and Hibernate for entity persistence and database interaction.

The main domain objects include:

- **User:** Stores user account and profile information.
- **Activity:** Stores logged workout information and associated metrics.
- **Recommendation:** Stores AI-generated recommendations, including improvements, suggestions, and safety information.

The application uses Hibernate's `ddl-auto=update` configuration to update the database schema based on entity mappings.

For production, the database is hosted on Neon PostgreSQL.

---

## ⚙️ Getting Started

Follow these steps to run the backend locally.

### Prerequisites

Make sure you have the following installed:

- Java (version compatible with the Maven project configuration)
- Maven (or use the included Maven Wrapper)
- PostgreSQL
- Git
- Ollama (optional, for local AI inference)

### 1. Clone the repository

```bash
git clone https://github.com/D-a-k-s-h/Fitness-Monolith-Backend
cd fitness-monolith
```

### 2. Configure environment variables

The application uses environment variables to configure database connectivity and AI integration.

Create the following environment variables:

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL JDBC connection URL |
| `DB_USER` | PostgreSQL username |
| `DB_PWD` | PostgreSQL password |
| `AI_PROVIDER` | AI provider selection: `LOCAL` or `CLOUD` |
| `AI_BASE_URL` | Base URL of the selected AI provider |
| `AI_MODEL` | Model identifier |
| `AI_API_KEY` | API key for cloud inference; not required for local Ollama |

Example local configuration:

```properties
DB_URL=jdbc:postgresql://localhost:5432/fitness_monolith
DB_USER=postgres
DB_PWD=your_database_password

AI_PROVIDER=LOCAL
AI_BASE_URL=http://localhost:11434
AI_MODEL=qwen3:1.7b
AI_API_KEY=
```

**Important:** Configure these values in your local environment or IDE run configuration. Do not commit credentials or API keys to GitHub.

### 3. Configure Ollama (Local AI)

If you want to use local AI inference, install Ollama and pull the required model.

```bash
ollama pull qwen3:1.7b
```

Start the Ollama service and ensure it is accessible at:

```text
http://localhost:11434
```

The backend will communicate with Ollama using its local API.

### 4. Configure Groq (Cloud AI)

To use Groq for cloud inference:

1. Create an API key through the [Groq Console](https://console.groq.com/).
2. Configure the following environment variables:

```properties
AI_PROVIDER=CLOUD
AI_BASE_URL=https://api.groq.com/openai/v1
AI_MODEL=openai/gpt-oss-20b
AI_API_KEY=your_groq_api_key
```

The backend uses the Chat Completions endpoint:

```text
/chat/completions
```

The API key is passed through the Authorization Bearer header.

### 5. Build the application

Using Maven Wrapper:

**Windows:**
```bash
mvnw.cmd clean package
```

**Linux / macOS:**
```bash
./mvnw clean package
```

### 6. Run the application

```bash
java -jar target/*.jar
```

Alternatively, run the main Spring Boot application class directly from your IDE.

The application starts using the configured database and AI provider settings.

---

## 🐳 Docker Deployment

The backend is containerized using Docker and deployed on Render.

Docker allows the application to run in a consistent environment without requiring a separate Java runtime setup on the hosting platform.

Build the Docker image:

```bash
docker build -t fitness-monolith-backend .
```

Run the container:

```bash
docker run -p 8080:8080 \
  -e DB_URL="your_database_url" \
  -e DB_USER="your_database_user" \
  -e DB_PWD="your_database_password" \
  -e AI_PROVIDER="CLOUD" \
  -e AI_BASE_URL="https://api.groq.com/openai/v1" \
  -e AI_MODEL="openai/gpt-oss-20b" \
  -e AI_API_KEY="your_groq_api_key" \
  fitness-monolith-backend
```

Make sure the required environment variables are configured in the deployment environment.

### Production Infrastructure

| Component | Platform |
|---|---|
| Backend | Render |
| Database | Neon PostgreSQL |
| Containerization | Docker |
| Cloud AI inference | Groq |
| Frontend | Vercel |

---

## 📖 API Documentation

The backend includes Springdoc OpenAPI for API documentation.

Once the application is running, access the Swagger UI at:

```text
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

Use the API documentation to explore available endpoints, request payloads, response structures, and authentication requirements.

For protected APIs, provide the JWT access token using the Bearer authentication scheme.

---

## 🔐 Security Considerations

- JWT-based authentication for protected API access.
- Spring Security for request authorization.
- User-specific access to application resources.
- Passwords should be stored using secure password hashing.
- Environment variables are used for sensitive configuration.
- AI provider credentials are not exposed to the frontend.

---

## 🔗 Related Links

- **Frontend:** [Fitness Monolith – Live Application](https://fitness-monolith-ten.vercel.app/)
- **Frontend Repository:** [GitHub Repository](https://github.com/D-a-k-s-h/Fitness-Monolith)
- **Backend Repository:** This repository.

---

## 👨‍💻 Author

**Daksh Kumar**

Software Developer | MERN & Java Spring Boot

- LinkedIn: [Daksh Kumar](https://www.linkedin.com/in/daksh-k-789083257/)
- GitHub: [D-a-k-s-h](https://github.com/D-a-k-s-h)

---

## 📌 Future Improvements

Fitness Monolith is an evolving project. Some areas for further improvement include:

- Expanding activity analytics and progress insights.
- Improving AI recommendation quality and consistency.
- Adding more comprehensive automated tests.
- Further optimizing API performance and database queries.
- Strengthening monitoring and error-handling capabilities.

---

**Built with Java, Spring Boot, PostgreSQL, and a passion for learning and building.** 💪
