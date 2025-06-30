# Cinerama Frontend

Frontend structure for the **Cinerama** project.

## 📂 Project Structure

```
src/ 
├── assets/ # Static files (images, logos, fonts) 
├── components/ # Reusable UI components
├── hooks/ # Custom React hooks 
├── models/ # Data models 
├── pages/ # Application pages (Home, About, etc.) 
├── services/ # API services 
├── utils/ # Helper utilities 
├── App.js # Main React component 
└── index.js # Entry point
```


## 🗝️ Environment Variables (.env.local)

This project uses environment variables for configuration. **Sensitive or local configuration should be placed in a `.env.local` file** at the project root. This file is included in `.gitignore` by default, so your secrets and local settings will not be committed to version control.

Example `.env.local`:

```
REACT_APP_PAYPAL_CLIENT_ID=your-paypal-client-id
DANGEROUSLY_DISABLE_HOST_CHECK=true
```

- **REACT_APP_PAYPAL_CLIENT_ID**: Your PayPal Client ID (never the secret). Required to enable PayPal payments in the frontend.
- **DANGEROUSLY_DISABLE_HOST_CHECK**: (optional) Allows proxy usage in development environments.

> **Note:** Never commit sensitive information such as secrets or passwords to the frontend or to your repository. Use `.env.local` for local/private configuration.

## 🚀 Getting Started

Install project dependencies:

```bash
npm install
```

Run the development server:

```bash
npm start
```

The app will be available at http://localhost:3000.

## 🛠️ Notes
- Components import their own .css files.

- Pages are placed under /pages; reusable elements under /components.

- This project was bootstrapped with Create React App.

- Future improvements: implement routing, global state management, and API integration.

---


