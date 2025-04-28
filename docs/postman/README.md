# 📬 Postman Collections - Cinerama Backend

This folder contains Postman collections used for testing the authentication API endpoints of the Cinerama backend project.

## 📂 Collection Available

- **Cinerama Auth API Collection**
  - Allows testing user registration, verification, and login endpoints.
  - Supports dynamic environment variables for easier testing across different setups.

## 🛠️ How to Use

1. Open **Postman**.
2. Click on **Import**.
3. Select the file `cinerama-auth-collection.json` located in this folder.
4. Create a new **Environment** in Postman with the following variables:

| Variable Name       | Example Value              | Purpose                        |
|---------------------|----------------------------|--------------------------------|
| `baseUrl`           | `localhost:8080`           | Base URL of the backend server |
| `userName`          | `John Doe`                 | Test user name                 |
| `userEmail`         | `john.doe@example.com`     | Test user email                |
| `userPassword`      | `SecurePass123`            | Test user password             |
| `verificationCode`  | `PASTE_FROM_DB`            | Verification code (from DB)    |

5. Select the environment before sending requests.

## 📋 Notes

- Ensure the backend server is running at the specified `baseUrl`.
- The verification code must be retrieved manually from the database after user registration.
- Future collections will cover booking, movie browsing, and payment modules.

---

# 🚀 Happy Testing!
