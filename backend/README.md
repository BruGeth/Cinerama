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

---

## 🧩 Planned Libraries

- Google Guava (planned)
- Apache POI (planned)
- Apache Commons (planned)

---

## 🚀 Running the Backend Locally

1. Make sure you have **MySQL** installed and running.
2. Create a database called `cinerama_db` (or adjust the name in `application.yml`).
3. Configure your local environment using a personal `application-local.yml` file.

---

## 🔐 Local Configuration (`application-local.yml`)

Create a new file in:

```bash
src/main/resources/application-local.yml
```

Use the following structure:

```yaml
spring:
  datasource:
    username: your_mysql_user
    password: your_mysql_password

  mail:
    username: your_email@gmail.com
    password: your_gmail_app_password
```

>Do not commit this file. It is ignored by `.gitignore` for security.

You can use the provided `application-local-example.yml` as a template.

---

## 📫 Email Configuration (Gmail SMTP)
Make sure to [create an App Password in Gmail](https://myaccount.google.com/apppasswords
) and enable 2FA.

The backend will use `JavaMailSender` to send real verification emails when the registration flow is triggered.

---

## ▶️ Start the application

```bash
mvn spring-boot:run
```
The backend should now be running at `http://localhost:8080`.

---

## 📋 Notes
- Log files are generated under the `/logs` directory, separated by log levels (INFO, DEBUG, ERROR).

- The `logs/` directory is excluded from version control via (`.gitignore`).

- Spring Security is configured with a basic setup. JWT authentication may be added later.

- Multi-database support (MySQL and Oracle) is planned for future versions.

---

## 📚 Useful Maven Commands

```bash
# Clean and build the project
mvn clean install

# Run tests
mvn test
```
---