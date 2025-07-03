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

### 3. Local Configuration

Create a file at `src/main/resources/application-local.yml` with the following structure:

```yaml
server:
    port: 8080 # Change if needed
spring:
  datasource:
    url: jdbc:mysql://localhost:PORT/cinerama_db?useSSL=false&serverTimezone=America/Lima # Adjust the URL as needed
    username: your_mysql_user
    password: your_mysql_password

  mail:
    username: your_email@gmail.com
    password: your_gmail_app_password
    
jwt:
    secret: your_jwt_secret
paypal:
  client-id: tu_paypal_client_id
  client-secret: tu_paypal_client_secret
```

>Do not commit this file. It is ignored by `.gitignore` for security.

You can use the provided `application-local-example.yml` as a template.

---

## 📫 Email Configuration (Gmail SMTP)

1. Enable 2-Step Verification on your Google account.
2. Generate an [App Password](https://myaccount.google.com/apppasswords).
3. Use this password in the `spring.mail.password` field.

The backend will send real verification emails during user registration.

---

## ▶️ Running the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

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