import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import About from "./pages/About";
import NotFound from "./pages/NotFound";
import VerifyEmail from "./pages/VerifyEmail";
import Navbar from "./Components/Navbar";
import Footer from "./Components/Footer";

const App = () => {
  return (
    <Router>
      <Navbar />
      <main style={{ padding: "2rem", minHeight: "80vh" }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/verify" element={<VerifyEmail />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
};

export default App;
