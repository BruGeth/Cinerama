import "../styles/Home.css";
import Slider from "../components/Slider";
import PromoSlider from "../components/PromoSlider";
import { Link } from "react-router-dom";
import { movies } from '../Components/DataMovie';
const Home = () => {
  return (
    <section className='home-wrapper'>
      {/* Slider section at the top of the homepage */}
      <Slider />
      <div className="home-container">
        {/* Promotions Section */}
        <section className="home-promotions" style={{ padding: '2rem' }}>
          <h2 className="home-title">OUR PROMOTIONS</h2>
          <PromoSlider />
        </section>

        {/* Section with movie cards */}
        <section className="home-card-section">
          <h2 className="home-subtitle">Now Showing</h2>
          <div className="home-card-container">


            {movies.map((movie) => (
              <div key={movie.id} className="home-card">
                <img src={movie.image} alt={movie.title} className="home-card-image" />
                <div className="home-card-text">
                  <h3>{movie.title}</h3>
                  <p>{movie.description_movie}</p>
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
