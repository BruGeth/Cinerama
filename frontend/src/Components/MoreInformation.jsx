import { useParams, useNavigate } from "react-router-dom";
import { FaTicketAlt } from "react-icons/fa";
import "../styles/MoreInformation.css";
import { useMovies } from "../hooks/useMovies"; // Nuevo import

const MoreInformation = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { movies: movie, loading } = useMovies(id);

  const handleBuyClick = () => {
    navigate(`/billboard/${movie.id}`);
  };

  if (loading) {
    return (
      <section className="more-info-wrapper">
        <h2>Cargando...</h2>
      </section>
    );
  }

  if (!movie) {
    return (
      <section className="more-info-wrapper">
        <h2>Película no ubicada</h2>
      </section>
    );
  }

  return (
    <section className="more-info-wrapper">
      {/* Movie trailer */}
      <div className="more-info-trailer">
        <iframe
          width="100%"
          height="400"
          src={movie.trailerUrl}
          title={`Tráiler ${movie.title}`}
          frameBorder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
        ></iframe>
      </div>

      {/* Movie details */}
      <div className="more-info-content">
        <img
          className="more-info-image"
          src={movie.imageUrl}
          alt={movie.title}
        />
        <div className="more-info-text">
          <h1>{movie.title}</h1>
          <p>{movie.descriptionMovie}</p>
          <button className="buy-button" onClick={handleBuyClick}>
            <FaTicketAlt style={{ marginRight: '8px' }} />
            Comprar
          </button>
        </div>
      </div>
    </section>
  );
};

export default MoreInformation;