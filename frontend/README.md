# 🎬 Cinerama Frontend

This is the frontend for the **Cinerama** cinema web application, built with **React**. It allows users to browse movies, book tickets, manage events, and more.

---

## 📂 Project Structure

```
src/
├── components/         # Reusable UI components
│   └── admin/          # Admin-specific components
├── context/            # React context providers
├── hooks/              # Custom React hooks
├── layouts/            # Layout components
├── pages/              # Application pages (Home, About, etc.)
│   └── admin/          # Admin-specific pages
├── routes/             # Route definitions and navigation
├── services/           # API services
├── styles/             # Global and shared styles
├── utils/              # Helper utilities
├── App.jsx             # Main React component
├── App.css             # Main component styles
├── App.test.js         # Main component tests
├── index.js            # Entry point
├── index.css           # Global styles
├── logo.svg            # Logo asset
├── reportWebVitals.js  # Web vitals reporting
└── setupTests.js       # Test setup
```

---

## ⚙️ Requirements

- **Node.js** (v16 or higher recommended)
- **npm** (v8 or higher recommended)

---

## 📦 Dependencies

Main dependencies used in this project:

- [`react`](https://react.dev/) ^19.1.0
- [`react-dom`](https://react.dev/) ^19.1.0
- [`react-router-dom`](https://reactrouter.com/) ^7.5.2
- [`axios`](https://axios-http.com/) ^1.9.0
- [`@paypal/react-paypal-js`](https://github.com/paypal/react-paypal-js) ^8.8.3
- [`canvas-confetti`](https://www.npmjs.com/package/canvas-confetti) ^1.9.3
- [`react-icons`](https://react-icons.github.io/react-icons/) ^5.5.0
- [`jwt-decode`](https://www.npmjs.com/package/jwt-decode) ^4.0.0

For testing:

- [`@testing-library/react`](https://testing-library.com/docs/react-testing-library/intro/) ^16.3.0
- [`@testing-library/jest-dom`](https://testing-library.com/docs/ecosystem-jest-dom/) ^6.6.3
- [`@testing-library/user-event`](https://testing-library.com/docs/user-event/intro/) ^13.5.0
- [`@testing-library/dom`](https://testing-library.com/docs/dom-testing-library/intro/) ^10.4.0
- [`web-vitals`](https://www.npmjs.com/package/web-vitals) ^2.1.4

---

## 🗝️ Environment Variables

This project uses environment variables for configuration. **Sensitive or local configuration should be placed in a `.env.local` file** at the project root. This file is included in `.gitignore` by default, so your secrets and local settings will not be committed to version control.

Example `.env.local`:

```
REACT_APP_PAYPAL_CLIENT_ID=your-paypal-client-id
DANGEROUSLY_DISABLE_HOST_CHECK=true
```

- **REACT_APP_PAYPAL_CLIENT_ID**: Your PayPal Client ID (never the secret). Required to enable PayPal payments in the frontend.
- **DANGEROUSLY_DISABLE_HOST_CHECK**: (optional) Allows proxy usage in development environments.

> **Note:** Never commit sensitive information such as secrets or passwords to the frontend or to your repository. Use `.env.local` for local/private configuration.

---

## 🚀 Getting Started

1. **Install dependencies:**

   ```bash
   npm install
   ```

2. **Set up environment variables:**

   - Copy `.env.example` to `.env.local` and fill in your values.

3. **Run the development server:**

   ```bash
   npm start
   ```

   The app will be available at [http://localhost:3000](http://localhost:3000).

---

## 🛠️ Notes

- Each component imports its own `.css` file for styles.
- Pages are placed under `/pages`; reusable elements under `/components`.
- This project was bootstrapped with [Create React App](https://create-react-app.dev/).
- The backend API is expected to run at `http://localhost:8080` (see the `proxy` setting in [`package.json`](package.json)).
- For PayPal integration, you must provide a valid client ID in your `.env.local`.
- Future improvements: implement advanced routing, global state management, and further API integration.

---

## 📄 License

This project is for educational/demo purposes. Please check with the project owner for licensing details.


