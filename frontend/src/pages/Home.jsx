import React from "react";
import "../styles/Home.css";
import Slider from "../Components/Slider";
import PromoSlider from "../Components/PromoSlider";

const Home = () => {
  return (
    <section className='home-wrapper'>
      {/* Slider section at the top of the homepage */}
      <Slider />
      <div className="home-container">
        {/* PROMOTIONS SECTION */}
        <section className="home-promotions" style={{ padding: '2rem' }}>
          <h2 className="home-title">OUR PROMOTIONS</h2>
          <PromoSlider />
        </section>

        {/* SECTION WITH MOVIE CARDS */}
        <section className="home-card-section">
          <h2 className="home-subtitle">Now Showing</h2>
          <div className="home-card-container">

            {/* Movie Card: Until Dawn */}
            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/until_dawn-880447124-large.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>UNTIL DAWN: NIGHT OF TERROR</h3>
                <p>The story follows a group of friends trapped in a deadly cycle trying to survive a horrific night...</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> More details
                </button>
              </div>
            </div>

            {/* Movie Card: Star Wars */}
            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/star_wars_episode_iii_revenge_of_the_sith-477144354-large.jpg"
                alt="Visión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>STAR WARS: EPISODE III - REVENGE OF THE SITH</h3>
                <p>The fall of Anakin Skywalker to the dark side and the rise of Darth Sidious as emperor of the Galactic Empire. It also shows the destruction of the Jedi Order, a plan carefully orchestrated by Palpatine.</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> More details
                </button>
              </div>
            </div>

            {/* Movie Card: Minecraft */}
            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/una%20pelicula%20de%20minecraft.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>A MINECRAFT MOVIE</h3>
                <p>The story of four misfits who, after being dragged into a blocky world, must learn to survive and work together to return to their own.</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> More details
                </button>
              </div>
            </div>

            {/* Movie Card: Thunderbolts */}
            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/thunderbolts-poster-66f184dbe403e.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>THUNDERBOLTS</h3>
                <p>Black Widow assassin Yelena (Florence Pugh) works for Valentina Allegra de Fontaine. Her latest mission is to blow up a lab in Southeast Asia...</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> More details
                </button>
              </div>
            </div>

            {/* Movie Card: The Amateur */}
            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/amateur.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>THE AMATEUR: OPERATION VENGEANCE</h3>
                <p>Charlie Heller, a brilliant but reclusive CIA decoder, is forced to take justice into his own hands after his wife's death.</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> More details
                </button>
              </div>
            </div>

            {/* Duplicate Movie Card: Until Dawn (Different description) */}
            <div className="home-card">
              <img
                src="https://www.cinerama.com.pe/_admin/assets/images/peliculas/until_dawn-880447124-large.jpg"
                alt="Misión"
                className="home-card-image"
              />
              <div className="home-card-text">
                <h3>UNTIL DAWN: NIGHT OF TERROR</h3>
                <p>A group of friends trapped in a time loop face a masked killer. Every time they die, the night resets.</p>
                <button className="home-details-button">
                  <i className="fa fa-info-circle"></i> More details
                </button>
              </div>
            </div>

          </div>
        </section>
      </div>
    </section>
  );
};

export default Home;
