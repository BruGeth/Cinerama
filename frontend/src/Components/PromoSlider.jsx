import React, { useState, useEffect } from 'react';
import '../styles/PromoSlider.css'; 

const PromoSlider = () => {
    const slides = [
        {
            image: '/images/PromoSlider1.jpeg',
        },
        {
            image: '/images/PromoSlider2.jpg',
        },
    ];

    const [animating, setAnimating] = useState(false);
    const [current, setCurrent] = useState(0);

    
    useEffect(() => {
        const interval = setInterval(() => {
          if (!animating) {
            nextSlide();
          }
        }, 3000);
      
        return () => clearInterval(interval);
      }, [animating]);

    const nextSlide = () => {
        setAnimating(true);
        setTimeout(() => {
            setCurrent((prev) => (prev + 1) % slides.length);
            setAnimating(false);
        }, 300);
    };

    const prevSlide = () => {
        setAnimating(true);
        setTimeout(() => {
            setCurrent((prev) => (prev - 1 + slides.length) % slides.length);
            setAnimating(false);
        }, 300);
    };

    return (
        <div className="slider promotions-slider">
            <button className="slider-button prev" onClick={prevSlide}>
                &#10094;
            </button>

            <div className="slider-content">
                <img
                    src={slides[current].image}
                    alt={`Promoción ${current + 1}`}
                    className={`promo-image ${animating ? 'fade-out' : 'fade-in'}`}
                />
                <button className="buy-button">Más información</button>
            </div>

            <button className="slider-button next" onClick={nextSlide}>
                &#10095;
            </button>
        </div>
    );
};

export default PromoSlider;
