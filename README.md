# AI Support Ticket Management System

A production-style **Spring Boot REST API** for managing customer support tickets with **JWT authentication, role-based authorization, AI-powered ticket analysis, automated response generation, pagination, search, validation, exception handling, Swagger/OpenAPI documentation, Docker, and cloud deployment**.

The application uses **Google Gemini** for AI capabilities and **TiDB Cloud** as the cloud-compatible MySQL database.

---

## 🚀 Live Application

## 🏗️ Architecture

![System Architecture](docs/architecture.png)


**Swagger UI:**  
[https://ai-support-ticket-management-6w2v.onrender.com/swagger-ui/index.html](https://ai-support-ticket-management-6w2y.onrender.com/swagger-ui/index.html#/)

**Health Check:**  
https://ai-support-ticket-management-6w2v.onrender.com/actuator/health

**Backend API:**  
[https://ai-support-ticket-management-6w2v.onrender.com](https://ai-support-ticket-management-6w2y.onrender.com/)

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![Render](https://img.shields.io/badge/Deployed-Render-purple)
![Tests](https://img.shields.io/badge/Tests-44%2F44-success)


> Note: The application is deployed on Render's free tier. The service may sleep after a period of inactivity, so the first request after inactivity may take longer.

---

## 📌 Project Overview

The **AI Support Ticket Management System** is a backend application designed to help support teams manage customer issues efficiently.

The system provides APIs for:

- Creating support tickets
- Viewing tickets
- Updating tickets
- Deleting tickets
- Searching tickets
- Pagination and sorting
- JWT-based authentication
- Role-based authorization
- AI-powered ticket analysis
- AI-generated customer responses
- Input validation
- Centralized exception handling
- API documentation with Swagger
- Health monitoring
- Docker-based deployment

The project follows a layered Spring Boot architecture:

```text
Client / Postman / Swagger
            |
            v
      REST Controller
            |
            v
         Service
            |
            v
       Repository
            |
            v
      TiDB / MySQL


Ticket
   |
   v
AI Controller
   |
   v
AI Service
   |
   v
Google Gemini API
   |
   v
AI Analysis
   |
   +---- Category
   +---- Priority
   +---- Sentiment
   +---- Summary
   +---- Suggested Response


                        Architecture
                        -------------


                         ┌──────────────────────┐
                         │      Client          │
                         │ Postman / Swagger    │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │   Spring Security   │
                         │      JWT Filter      │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │     Controllers      │
                         │                      │
                         │ AuthController       │
                         │ TicketController     │
                         │ AIController         │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │       Services       │
                         │                      │
                         │ AuthService          │
                         │ TicketService        │
                         │ AIService            │
                         └──────────┬───────────┘
                                    │
                         ┌──────────┴───────────┐
                         │                      │
                         ▼                      ▼
                ┌──────────────────┐   ┌──────────────────┐
                │ TicketRepository │   │  UserRepository  │
                └────────┬─────────┘   └────────┬─────────┘
                         │                      │
                         └──────────┬───────────┘
                                    ▼
                         ┌──────────────────────┐
                         │     TiDB Cloud       │
                         │   MySQL Compatible   │
                         └──────────────────────┘

                         AI Flow
                            │
                            ▼
                    ┌──────────────────┐
                    │  Google Gemini   │
                    └──────────────────┘

## 🧠 Technical Challenges Solved

### Secure API Access
Implemented stateless JWT authentication with role-based authorization for USER, SUPPORT_AGENT, and ADMIN roles.

### AI Reliability
Implemented retry handling for Gemini API failures to handle temporary external-service failures without immediately failing the request.

### Database Performance
Added indexes on frequently queried ticket fields such as status, priority, and created timestamp.

### API Scalability
Implemented server-side pagination, sorting, and search to avoid loading the complete ticket dataset into memory.

### Production Configuration
Externalized database credentials, JWT secrets, and Gemini API keys using environment variables instead of hard-coding sensitive values.

### Cloud Deployment
Containerized the application using Docker and deployed it to Render with TiDB Cloud as the persistent database.



🔮 Future Enhancements
Potential future improvements:
- Refresh token mechanism
- Email notifications
- Ticket assignment to support agents
- Ticket comments/history
- Audit logging
- Redis caching
- Rate limiting
- Kafka-based event processing
- File attachments
- Advanced analytics dashboard
- AI confidence scoring
- AI prompt versioning
- Conversation history
- Automated ticket categorization
- Kubernetes deployment
- CI/CD pipeline
- Observability with metrics and distributed tracing



