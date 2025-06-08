import React from "react";
import "../styles/Home.css";
import Slider from "../Components/Slider";
import PromoSlider from "../Components/PromoSlider";
import { Link } from "react-router-dom";
import { movies } from "../Components/movieData";
const Home = () => {
  return (
    <section className='home-wrapper'>
      <Slider />
      <div className="home-container">
        {/* HISTORY */}
        <section className="home-promotions" style={{ padding: '2rem' }}>
          <h2 className="home-title">NUESTRAS PROMOCIONES</h2>
          <PromoSlider />
        </section>

        {/* NEW SECTION WITH CARDS */}
        <section className="home-card-section">
          <h2 className="home-subtitle">Películas en Cartelera</h2>
          <div className="home-card-container">


            {movies.map((movie) => (
              <div key={movie.id} className="home-card">
                    <img src={movie.image} alt={movie.title} className="home-card-image" />
                <div className="home-card-text">
                  <h3>{movie.title}</h3>
                  <p>{movie.description}</p>
                  <Link to={`/more-information/${movie.id}`}>
                    <button className="home-details-button">
                      <i className="fa fa-info-circle"></i> Más detalles
                    </button>
                  </Link>
                </div>
              </div>
            ))}

          </div>
        </section>
      </div>
    </section>
  );
};

export default Home;
