# 🎬 Cinerama - Cinema Web Application

Welcome to **Cinerama**, an online platform for cinema ticketing and movie browsing.

This repository contains both the frontend and backend for the Cinerama project:

- **Frontend**: A React application (located in `/frontend`)
- **Backend**: A Spring Boot application with MySQL integration (located in `/backend`)

---

## 🏗️ Project Structure

```
Cinerama/
├── frontend/    # React client-side application
│   └── README.md
├── backend/     # Spring Boot server-side application
│   └── README.md
├── docs/        # Project documentation and resources
│   └── postman/ # Postman collections for API testing
├── .github/     # GitHub-specific configuration (workflows, issue templates, etc.)
├── README.md    # General project documentation (this file)
└── ...          # Other project files
```

---

## ⚙️ Technologies Used

- **Frontend:** React, React Router, Axios, PayPal JS SDK, Testing Library
- **Backend:** Spring Boot, Spring Data JPA, Spring Security, Logback, Spring Mail, Apache POI, Lombok, JWT, PayPal SDK
- **Database:** MySQL (Oracle support planned)
- **Other:** JWT authentication, RESTful APIs, Email notifications

---

## 🚀 Getting Started

### Prerequisites

- **Node.js** (v16 or higher) and **npm** (v8 or higher) for the frontend
- **Java 17+** and **Maven 3.8+** for the backend
- **MySQL** server running locally or accessible remotely

---

### Backend Setup

1. **Configure the database:**
   - Create a MySQL database (e.g., `cinerama_db`).
   - Copy `application-local-example.yml` to `application-local.yml` in `backend/src/main/resources/` and update your DB credentials, mail, JWT, and PayPal settings.

2. **Run the backend:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   The API will be available at [http://localhost:8080](http://localhost:8080) ( Port could be modified in `application-local.yml` ).

> For more details, see [backend/README.md](./backend/README.md).

---

### Frontend Setup

1. **Install dependencies:**
   ```bash
   cd frontend
   npm install
   ```

2. **Set up environment variables:**
   - Copy `.env.example` to `.env.local` in the `frontend` folder and fill in your values (e.g., PayPal client ID).

3. **Run the frontend:**
   ```bash
   npm start
   ```
   The app will be available at [http://localhost:3000](http://localhost:3000).

> For more details, see [frontend/README.md](./frontend/README.md).

---

## 🧩 Planned Libraries

- **Google Guava:** Utilities and caching (backend)
- **Apache Commons:** Enhanced utilities (backend)
- **Oracle Database:** Planned support

---

## 📋 Additional Notes

- The `logs/` directory is excluded from version control via `.gitignore`.
- Each module (`frontend` and `backend`) contains its own `README.md` with detailed setup, dependencies, and usage instructions.
- Please do not commit sensitive information (such as credentials or secrets) to the repository.
- Contributions and suggestions are welcome!

---

## 📄 License

This project is for educational/demo purposes. Please check with the project owner for licensing details.
