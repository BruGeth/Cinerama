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

    const [animating, setAnimating] = useState(false); // Prevents multiple animations at the same time
    const [current, setCurrent] = useState(0); // Tracks the current slide index

    useEffect(() => {
        const interval = setInterval(() => {
            if (!animating) {
                nextSlide(); // Move to the next slide if not animating
            }
        }, 3000); // Slide changes every 3 seconds

        return () => clearInterval(interval); // Clear the interval on unmount
    }, [animating]);

    const nextSlide = () => {
        setAnimating(true); // Set animation flag to true
        setTimeout(() => {
            setCurrent((prev) => (prev + 1) % slides.length); // Advance to the next slide
            setAnimating(false); // Reset animation flag
        }, 300); // Transition time
    };

    const prevSlide = () => {
        setAnimating(true); // Set animation flag to true
        setTimeout(() => {
            setCurrent((prev) => (prev - 1 + slides.length) % slides.length); // Go to the previous slide
            setAnimating(false); // Reset animation flag
        }, 300); // Transition time
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
                    className={`promo-image ${animating ? 'fade-out' : 'fade-in'}`} // Apply fade-in/out class based on animation state
                />
                <button 
                    className="buy-button"
                    onClick={() => window.location.href = '/promotions'} // Redirect to promotions page
                >
                    Más información
                </button>
            </div>

            <button className="slider-button next" onClick={nextSlide}>
                &#10095;
            </button>
        </div>
    );
};

export default PromoSlider;
