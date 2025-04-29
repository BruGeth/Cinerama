import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from '../pages/Home';  
import About from '../pages/About';
import NotFound from '../pages/NotFound';

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;
