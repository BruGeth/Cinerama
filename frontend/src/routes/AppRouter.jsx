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
          <Route path="*" element={<NotFound />} />
          <Route path='/verify' element={<VerifyEmail/>}/>
          <Route path="/cartelera" element={<Cartelera />} />
          <Route path="/about" element={<About />} />
          <Route path="/cartelera/:id" element={<MovieDetail/>} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
};
export default AppRouter;