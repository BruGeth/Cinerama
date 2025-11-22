# 🎬 Cinerama Backend

This is the backend service for the **Cinerama** project, built with **Spring Boot** and **MySQL**.

---

## 🛠️ Technologies

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL Driver
- Logback for logging
- Spring Mail (for sending emails via SMTP)
- Apache POI
- Lombok (for reducing boilerplate code)
- JUnit 5 (for testing)
- Mockito (for mocking in tests)
- H2 Database (for in-memory testing)
- JWT (for authentication)
- PayPal SDK (for payment processing)

---

## 🧩 Planned Libraries

- Google Guava (planned)
- Apache Commons (planned)

---

## 🚀 Getting Started

### 1. Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **MySQL** installed and running

### 2. Database Setup

Create a database named `cinerama_db` (or update the name in your configuration).

### 3. Environment Configuration

**⚠️ IMPORTANT: All configurations now use environment variables - no hardcoded values!**

#### Step 1: Create your `.env` file

```bash
# In the backend/ directory
cp .env.example .env
```

#### Step 2: Configure your environment variables

Edit the `.env` file with your actual values:

```bash
# Server
SERVER_PORT=8080

# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=cinerama_db
DB_TIMEZONE=America/Lima
DB_USERNAME=root
DB_PASSWORD=your_password

# Email (Gmail SMTP)
EMAIL_USERNAME=your.email@gmail.com
EMAIL_PASSWORD=your_gmail_app_password

# JWT
JWT_SECRET=your_jwt_secret_key_min_256_bits
JWT_EXPIRATION=86400000

# PayPal
PAYPAL_CLIENT_ID=your_paypal_client_id
PAYPAL_CLIENT_SECRET=your_paypal_client_secret

# TMDB API
TMDB_API_KEY=your_tmdb_api_key

# CORS (comma-separated)
CORS_ALLOWED_ORIGINS=http://localhost:3000,exp://192.168.1.100:8081
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=Authorization,Content-Type,X-Requested-With
CORS_ALLOW_CREDENTIALS=true
CORS_MAX_AGE=3600
```

> **Security Note**: Never commit your `.env` file! It's already in `.gitignore`.

### 4. Choose Your Profile

The application supports multiple profiles for different environments:

#### **Local Profile** (Individual Development)
For your personal machine:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

#### **Dev Profile** (Team Development)
For shared development environment:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Profile Differences:**
| Feature | Local | Dev |
|---------|-------|-----|
| JPA DDL | update | validate |
| Show SQL | true | false |
| Logs | console | console + file |

📖 **Full documentation**: See [ENVIRONMENT_PROFILES.md](./ENVIRONMENT_PROFILES.md)

---

## 📫 Email Configuration (Gmail SMTP)

1. Enable 2-Step Verification on your Google account.
2. Generate an [App Password](https://myaccount.google.com/apppasswords).
3. Use this password in the `spring.mail.password` field.

The backend will send real verification emails during user registration.

---

## ▶️ Running the Application

### Quick Start

```bash
# With local profile (default in application.yml)
mvn spring-boot:run

# With specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
# or
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at `http://localhost:8080` (or your configured `SERVER_PORT`).

---

## 🗂️ Logging

- Log files are generated in the `/logs` directory, separated by log level (`INFO`, `DEBUG`, `ERROR`).
- The `logs/` directory is excluded from version control.

---

## 🔐 Security

- Basic security is configured with Spring Security.
- JWT authentication is planned for future releases.

---

## 🗃️ Multi-Database Support

- Support for MySQL and Oracle is planned for future versions.

---

## 📚 Useful Maven Commands

```bash
# Clean and build the project
mvn clean install

# Run tests
mvn test
```

---

## 🤝 Contributing

Contributions are welcome! Please open an [issue](https://github.com/BruGeth/Cinerama/issues) or [pull request](https://github.com/BruGeth/Cinerama/pulls) for suggestions or improvements.

---