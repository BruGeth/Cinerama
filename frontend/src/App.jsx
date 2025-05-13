import { AuthProvider } from './context/AuthContext'; // crea una carpeta context/ y coloca ahí el AuthContext
import AppRouter from './routes/AppRouter';

function App() {
  return (
    <AuthProvider>
      <AppRouter />
    </AuthProvider>
  );
}

export default App;
