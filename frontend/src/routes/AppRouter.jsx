import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from '../Components/Navbar';
import Footer from '../Components/Footer';
import Home from '../pages/Home';
import About from '../pages/About';
import NotFound from '../pages/NotFound';
import Register from "../pages/Register";
import Login from "../pages/Login";
import VerifyEmail from '../pages/VerifyEmail';
import Cartelera from '../pages/Cartelera';
import MovieDetail from '../pages/MovieDetail';
import Corporate from '../pages/Corporate';
import FestaRamaPage from '../pages/FestaRamaPage';
import FestaRamaPackages from '../pages/FestaRamaPackages';
import Profile from '../pages/Profile';
import PrivateRoute from '../utils/PrivateRoute';


const AppRouter = () => {
  return (
    <Router>
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path='/verify' element={<VerifyEmail />} />
          <Route path="/profile" element={
            <PrivateRoute>
              <Profile />
            </PrivateRoute>
          } />
          <Route path="*" element={<NotFound />} />
          <Route path='/verify' element={<VerifyEmail/>}/>
          <Route path="/cartelera" element={<Cartelera />} />
          <Route path="/about" element={<About />} />
          <Route path="/cartelera/:id" element={<MovieDetail/>} />
          <Route path='/corporate' element={<Corporate/>}/>
          <Route path="/festaramapage" element={<FestaRamaPage />} />
          <Route path="/festarama/packages" element={<FestaRamaPackages />} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
};
export default AppRouter;