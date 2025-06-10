import { useState, useEffect, useCallback } from "react";
import { FaTicketAlt } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';
import "../styles/Slider.css";

const Slider = () => {
  const slides = [
    {
      id: 1,
      image: '/images/Home4.jpeg',
      title: 'Thunderbolts',
      description:
        'Un mundo sin Vengadores no significa que no haya un grupo de superhéroes. Hay un grupo y se llaman Thunderbolts.',
    },
    //... otros slides
  ];

  const [animating, setAnimating] = useState(false);
  const [current, setCurrent] = useState(0);
  const navigate = useNavigate();

  const nextSlide = useCallback(() => {
    setAnimating(true);
    setTimeout(() => {
      setCurrent((prev) => (prev + 1) % slides.length);
      setAnimating(false);
    }, 300);
  }, [slides.length]);

  const prevSlide = () => {
    setAnimating(true);
    setTimeout(() => {
      setCurrent((prev) => (prev - 1 + slides.length) % slides.length);
      setAnimating(false);
    }, 300);
  };

  const handleBuyClick = () => {
    const movieId = slides[current].id;
    navigate(`/cartelera/${movieId}`);
  };

  useEffect(() => {
    const interval = setInterval(() => {
      if (!animating) {
        nextSlide();
      }
    }, 3000); // every 3 seconds

    return () => clearInterval(interval); // cleaning
  }, [animating, nextSlide]); // restarts if animating changes

  return (
    <div className="slider">
      <button className="slider-button prev" onClick={prevSlide}>&#10094;</button>

      <div className="slider-content">
        <img
          src={slides[current].image}
          alt={`Slide ${current + 1}`}
          className={`slider-image ${animating ? "fade-out" : "fade-in"}`}
        />
        <div className="slider-overlay">
          <h1 className="slider-title"><span className="highlight">{slides[current].title}</span></h1>
          <p className="slider-description">{slides[current].description}</p>
          <button className="buy-button" onClick={handleBuyClick}>
            <FaTicketAlt style={{ marginRight: '8px' }} />
            Comprar
          </button>
        </div>
      </div>

      <button className="slider-button next" onClick={nextSlide}>&#10095;</button>
    </div>
  );
};

export default Slider;
