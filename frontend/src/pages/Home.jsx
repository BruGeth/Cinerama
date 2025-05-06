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
    <section className='chau'>
      <Slider />
      <div className="container">
        {/* HISTORY*/}
        <section className="promotions-section" style={{ padding: '2rem' }}>
          <h2 style={{ color: '#fff', fontSize: '2rem', marginBottom: '1rem' }}>NUESTRAS PROMOCIONES</h2>
          <PromoSlider />
        </section>

        {/* NEW SECTION WITH CARDS*/}
        <section className="card-section">
          <h2 className="subtitulo" style={{ textAlign: 'center', color: 'white' }}>Películas en Cartelera</h2>
          <div className="card-container">
          <div className="card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/until_dawn-880447124-large.jpg"
                alt="Misión"
                className="card-image"
              />
              <div className="card-text">
                <h3>UNTIL DAWN: NOCHE DE TERROR</h3>
                <p>La historia sigue a un grupo de amigos que se ven atrapados en un ciclo de terror al intentar sobrevivir una noche mortífera...</p>
                <button className="details-button" style={{ borderRadius: '25px' }}>
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>
  

          <div className="card">
            <img
              src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/star_wars_episode_iii_revenge_of_the_sith-477144354-large.jpg"
              alt="Visión"
              className="card-image"
            />
            <div className="card-text">
              <h3>STAR WARS: EPISODIO III - LA VENGANZA DE LOS SITH</h3>
              <p>La caída de Anakin Skywalker en el lado oscuro y el ascenso de Darth Sidious como emperador del primer Imperio Galáctico. También muestra la destrucción de la Orden Jedi, un plan cuidadosamente orquestado por Palpatine.</p>
              <button className="details-button" style={{ borderRadius: '25px' }}>
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
            </div>
          </div>
          <div className="card">
            <img
              src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/una%20pelicula%20de%20minecraft.jpg"
              alt="Misión"
              className="card-image"
            />
            <div className="card-text">
              <h3>UNA PELICULA DE MINECRAFT</h3>
              <p>Historia de cuatro inadaptados que, al ser arrastrados a un mundo de bloques, deben aprender a sobrevivir y trabajar juntos para regresar a su mundo</p>
              <button className="details-button" style={{ borderRadius: '25px' }}>
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
            </div>
          </div>
          <div className="card">
            <img
              src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/thunderbolts-poster-66f184dbe403e.jpg"
              alt="Misión"
              className="card-image"
            />
            <div className="card-text">
              <h3>THUNDERBOLTS</h3>
              <p> la viuda negra asesina Yelena (Florence Pugh) trabaja para Valentina Allegra de Fontaine (Julia Louis-Dreyfus); su último encargo es volar un laboratorio en el sudeste asiático. Al mismo tiempo, Valentina se enfrenta a la destitución de su cargo como directora de la CIA, y ella y su ayudante Mel.</p>
              <button className="details-button" style={{ borderRadius: '25px' }}>
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
            </div>
          </div>
          <div className="card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/amateur.jpg"
                alt="Misión"
                className="card-image"
              />
              <div className="card-text">
                <h3>THE AMATHEUR: OPERACIÓN VENGANZA</h3>
                <p>Charlie Heller, un brillante pero retraído descodificador de la CIA que, tras la muerte de su esposa en un atentado terrorista en Londres, se ve obligado a tomar la justicia por su cuenta.</p>
                <button className="details-button" style={{ borderRadius: '25px' }}>
                  <i className="fa fa-info-circle"></i> Más detalles
                </button>
              </div>
            </div>
            <div className="card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/until_dawn-880447124-large.jpg"
                alt="Misión"
                className="card-image"
              />
              <div className="card-text">
                <h3>UNTIL DAWN: NOCHE DE TERROR</h3>
                <p>La historia sigue a un grupo de amigos que se ven atrapados en un ciclo de terror al intentar sobrevivir una noche mortífera, donde un asesino enmascarado los persigue y mata uno a uno. Cada vez que mueren, regresan al inicio de la noche.</p>
                <button className="details-button" style={{ borderRadius: '25px' }}>
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
      </div >
    </section >
  );
};

export default Home;
