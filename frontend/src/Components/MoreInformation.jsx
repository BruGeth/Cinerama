import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import { movies} from "./Movies"; // Make sure the path is correct
import { FaTicketAlt } from "react-icons/fa";
import "../styles/MoreInformation.css";

const MoreInformation = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const movie = movies.find((m) => m.id === parseInt(id));

  const handleBuyClick = () => {
    navigate(`/cartelera/${movie.id}`);
  };

  if (!movie) {
    return (
      <section className="more-info-wrapper">
        <h2>Película no hubicada</h2>
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
          src={movie.trailer}
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
          src={movie.image}
          alt={movie.title}
        />
        <div className="more-info-text">
          <h1>{movie.title}</h1>
          <p>{movie.description}</p>
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
