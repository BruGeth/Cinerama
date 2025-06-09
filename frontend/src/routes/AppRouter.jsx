import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from '../Components/Navbar';
import Footer from '../Components/Footer';
import Home from '../pages/Home';
import About from '../pages/About';
import NotFound from '../pages/NotFound';
import Register from "../pages/Register";
import Login from "../pages/Login";
import VerifyEmail from '../pages/VerifyEmail';
import Confectionery from '../pages/Confectionery';
import Cartelera from '../pages/Cartelera';
import MovieDetail from '../pages/MovieDetail';
import Corporate from '../pages/Corporate';
import FestaRama from '../pages/FestaRama';
import FestaRamaPackages from '../pages/FestaRamaPackages';
import Profile from '../pages/Profile';
import PrivateRoute from '../utils/PrivateRoute';
import Cines from '../pages/Cines';
import Promotions from '../pages/Promotions';
import Events from '../pages/Events';
import SpecialFunctions from '../pages/SpecialFunctions';
import Advertising from '../pages/Advertising';

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
          <Route path='/confectionery' element={<Confectionery/>}/>
          <Route path="/movies" element={<Cartelera />} />
          <Route path="/about" element={<About />} />
          <Route path="/cartelera/:id" element={<MovieDetail/>} />
          <Route path='/corporate' element={<Corporate/>}/>
          <Route path="/festarama" element={<FestaRama />} />
          <Route path="/festarama/packages" element={<FestaRamaPackages />} />
          <Route path="/cinemas" element={<Cines />} /> 
          <Route path='/promotions' element={<Promotions/>} />
          <Route path='/events' element={<Events/>} />
          <Route path='/specialfunctions' element={<SpecialFunctions/>} />
          <Route path='/advertising' element={<Advertising/>} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
};
export default AppRouter;