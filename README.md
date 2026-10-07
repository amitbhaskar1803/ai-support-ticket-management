# AI Support Ticket Management System

A production-style **Spring Boot REST API** for managing customer support tickets with **JWT authentication, role-based authorization, AI-powered ticket analysis, automated response generation, pagination, search, validation, exception handling, Swagger/OpenAPI documentation, Docker, and cloud deployment**.

The application uses **Google Gemini** for AI capabilities and **TiDB Cloud** as the cloud-compatible MySQL database.

---

## 🚀 Live Application

**Backend API:**  
[https://ai-support-ticket-management-6w2v.onrender.com](https://ai-support-ticket-management-6w2y.onrender.com/)

**Swagger UI:**  
[https://ai-support-ticket-management-6w2v.onrender.com/swagger-ui/index.html](https://ai-support-ticket-management-6w2y.onrender.com/swagger-ui/index.html#/)

**Health Check:**  
https://ai-support-ticket-management-6w2v.onrender.com/actuator/health

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
