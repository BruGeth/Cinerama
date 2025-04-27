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

---

## 🧩 Planned Libraries

- Google Guava (planned)
- Apache POI (planned)
- Apache Commons (planned)

---

## 🚀 Running the Backend Locally

1. Make sure you have **MySQL** installed and running.
2. Create a database called `cinerama` (or adjust the database name in `application.yml`).
3. Configure the database credentials inside `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cinerama
    username: your_mysql_user
    password: your_mysql_password
```

4. Run the backend server:

```bash
mvn spring-boot:run
```

The backend should now be running at `http://localhost:8080`.

---

## 📋 Notes
- Log files are generated under the `/logs` directory, separated by log levels (INFO, DEBUG, ERROR).

- The `logs/` directory is excluded from version control (`.gitignore`).

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