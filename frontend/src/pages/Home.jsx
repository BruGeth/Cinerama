import React from 'react';
import '../styles/Home.css';
import Slider from '../Components/Slider';
import PromoSlider from '../Components/PromoSlider';

const Home = () => {

  {/* for video but no */}

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

            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/until_dawn-880447124-large.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>UNTIL DAWN: NOCHE DE TERROR</h3>
                <p>La historia sigue a un grupo de amigos que se ven atrapados en un ciclo de terror al intentar sobrevivir una noche mortífera...</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>

            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/star_wars_episode_iii_revenge_of_the_sith-477144354-large.jpg"
                alt="Visión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>STAR WARS: EPISODIO III - LA VENGANZA DE LOS SITH</h3>
                <p>La caída de Anakin Skywalker en el lado oscuro y el ascenso de Darth Sidious como emperador del primer Imperio Galáctico. También muestra la destrucción de la Orden Jedi, un plan cuidadosamente orquestado por Palpatine.</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>

            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/una%20pelicula%20de%20minecraft.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>UNA PELICULA DE MINECRAFT</h3>
                <p>Historia de cuatro inadaptados que, al ser arrastrados a un mundo de bloques, deben aprender a sobrevivir y trabajar juntos para regresar a su mundo</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>

            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/thunderbolts-poster-66f184dbe403e.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>THUNDERBOLTS</h3>
                <p>La viuda negra asesina Yelena (Florence Pugh) trabaja para Valentina Allegra de Fontaine. Su último encargo es volar un laboratorio en el sudeste asiático...</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>

            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/amateur.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>THE AMATHEUR: OPERACIÓN VENGANZA</h3>
                <p>Charlie Heller, un brillante pero retraído descodificador de la CIA, se ve obligado a tomar la justicia por su cuenta tras la muerte de su esposa.</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>

            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/until_dawn-880447124-large.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>UNTIL DAWN: NOCHE DE TERROR</h3>
                <p>Un grupo de amigos atrapados en un bucle temporal enfrentan a un asesino enmascarado. Cada vez que mueren, regresan al inicio de la noche.</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>

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
