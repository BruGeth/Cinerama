import "../styles/Home.css";
import Slider from "../components/Slider";
import PromoSlider from "../components/PromoSlider";
import { Link } from "react-router-dom";
import { useMovies } from "../hooks/useMovies"; // Nuevo import

const Home = () => {
  const { movies, loading } = useMovies();

  return (
    <section className='home-wrapper'>
      {/* Slider section at the top of the homepage */}
      <Slider />
      <div className="home-container">
        {/* Promotions Section */}
        <section className="home-promotions" style={{ padding: '2rem' }}>
          <h2 className="home-title">NUESTRAS PROMOCIONES</h2>
          <PromoSlider />
        </section>

        {/* Section with movie cards */}
        <section className="home-card-section">
          <h2 className="home-subtitle">Cartelera Actual</h2>
          <div className="home-card-container">
            {loading ? (
              <p>Cargando películas...</p>
            ) : (
              movies.map((movie) => (
                <div key={movie.id} className="home-card">
                  <img src={movie.imageUrl} alt={movie.title} className="home-card-image" />
                  <div className="home-card-text">
                    <h3>{movie.title}</h3>
                    <p>{movie.descriptionMovie}</p>
                    <Link to={`/more-information/${movie.id}`}>
                      <button className="home-details-button">
                        <i className="fa fa-info-circle"></i> Más detalles
                      </button>
                    </Link>
                  </div>
                </div>
              ))
            )}
          </div>
        </section>
      </div>
    </section>
  );
};

export default Home;