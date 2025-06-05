import React from "react";
import "../styles/Home.css";
import Slider from "../Components/Slider";
import PromoSlider from "../Components/PromoSlider";
import { Link } from "react-router-dom";
import { MOVIES } from "../Components/movies";
const Home = () => {

  /*{/* for video but no }*/


  // const [showModal, setShowModal] = useState(false);
  // const [trailerUrl, setTrailerUrl] = useState('');

  // const openTrailer = (url) => {
  //   setTrailerUrl(url);
  //   setShowModal(true);
  // };

  // const closeModal = () => {
  //   setShowModal(false);
  //   setTrailerUrl('');
  // };

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


            {MOVIES.map((movie) => (
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
        {/* for video but no */}
        {/* {showModal && (
          <div className="modal-overlay" onClick={closeModal}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
              <iframe
                width="560"
                height="315"
                src={trailerUrl}
                title="Tráiler"
                frameBorder="0"
                allow="autoplay; encrypted-media"
                allowFullScreen
              ></iframe>
              <button className="close-button" onClick={closeModal}>Cerrar</button>
            </div>
          </div>
        )} */}
      </div>
    </section>
  );
};

export default Home;
