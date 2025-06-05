import React, { useState, useEffect } from 'react';
import { FaTicketAlt } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom'; // Added for navigation
import '../styles/Slider.css';

const Slider = () => {
  // Added 'id' to associate each slide with its corresponding movie route
  const slides = [
    {
      id: 1,
      image: '/images/Home4.jpeg',
      title: 'Thunderbolts',
      description:
        'Un mundo sin Vengadores no significa que no haya un grupo de superhéroes. Hay un grupo y se llaman Thunderbolts.',
    },
    {
      id: 2,
      image: '/images/Home1.jpg',
      title: 'Destino Final: Lazos de Sangre',
      description:
        'Un adolescente tiene una visión de él y sus amigos muriendo en un accidente de avión. Previene el accidente, pero la muerte los persigue uno por uno.',
    },
    {
      id: 3,
      image: '/images/Home3.jpg',
      title: 'Karate Kid Leyendas',
      description:
        'Daniel LaRusso y su madre acaban de mudarse a Reseda, Los Ángeles, desde Newark, Nueva Jersey, al comenzar el año escolar.',
    },
    {
      id: 4,
      image: '/images/Home2.jpg',
      title: 'Star Wars: Episodio III - La venganza de los Sith',
      description:
        'El Canciller Palpatine fue secuestrado y el Maestro Jedi Obi-Wan Kenobi, acompañado de su aprendiz Anakin Skywalker, es enviado a rescatarlo en una misión, donde también debe eliminar a los Líderes Separatistas, el Conde Dooku y el General Grievous, para concluir el conflicto galáctico.',
    },
  ];

  const [animating, setAnimating] = useState(false);
  const [current, setCurrent] = useState(0);

  const navigate = useNavigate(); // Hook to handle navigation programmatically

  // Handles redirection to the movie detail page using the slide's ID
  const handleBuyClick = () => {
    const movieId = slides[current].id;
    navigate(`/cartelera/${movieId}`); // Navigates to /cartelera/:id
  };

  useEffect(() => {
    const interval = setInterval(() => {
      if (!animating) {
        nextSlide();
      }
    }, 3000); // Auto-advance every 3 seconds

    return () => clearInterval(interval); // Cleanup interval on unmount
  }, [animating]);

  const nextSlide = () => {
    setAnimating(true);
    setTimeout(() => {
      setCurrent((prev) => (prev + 1) % slides.length);
      setAnimating(false);
    }, 300); // Delay to allow animation effect
  };

  const prevSlide = () => {
    setAnimating(true);
    setTimeout(() => {
      setCurrent((prev) => (prev - 1 + slides.length) % slides.length);
      setAnimating(false);
    }, 300);
  };

  return (
    <div className="slider">
      <button className="slider-button prev" onClick={prevSlide}>
        &#10094;
      </button>

      <div className="slider-content">
        <img
          src={slides[current].image}
          alt={`Slide ${current + 1}`}
          className={`slider-image ${animating ? 'fade-out' : 'fade-in'}`}
        />
        <div className="slider-overlay">
          <h1 className="slider-title">
            <span className="highlight">{slides[current].title}</span>
          </h1>
          <p className="slider-description">{slides[current].description}</p>

          <button className="buy-button" onClick={handleBuyClick}> {/* Triggers redirection on click */}
            <FaTicketAlt style={{ marginRight: '8px' }} />
            Comprar
          </button>
        </div>
      </div>

      <button className="slider-button next" onClick={nextSlide}>
        &#10095;
      </button>
    </div>
  );
};

export default Slider;
