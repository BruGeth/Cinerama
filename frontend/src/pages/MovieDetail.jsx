import { useState, useEffect } from "react"; // Import React and useState for managing component state
import { useParams, useNavigate } from "react-router-dom"; // Import hooks for routing functionality
import { useMovies } from "../hooks/useMovies"; // Import custom hook for fetching movie data from API
import "../styles/MovieDetail.css"; // Import CSS for styling

const MovieDetail = () => {
  const { id } = useParams(); // Extract movie ID from the URL
  const navigate = useNavigate(); // Hook for handling navigation
  const { movies: movie, loading } = useMovies(id); // Fetch movie data using custom hook

  // State to track the selected date for showtimes
  const [selectedDate, setSelectedDate] = useState(0);
  // State to store grouped showtimes by format for the selected date
  const [groupedShowtimes, setGroupedShowtimes] = useState({
    "2D": [],
    "3D": [],
    XD: [],
  });

  // Generate an array of the next 7 days for showtime selection
  const days = Array.from({ length: 7 }, (_, i) => {
    const date = new Date();
    date.setDate(date.getDate() + i);
    return {
      id: i,
      dateObj: new Date(date), // Save the Date object for comparison
      dayName: date
        .toLocaleDateString("es-PE", { weekday: "short" })
        .slice(0, 3)
        .toUpperCase(),
      formattedDate: `${date.toLocaleDateString("es-PE", {
        day: "2-digit",
      })} ${date
        .toLocaleDateString("es-PE", { month: "short" })
        .replace(".", "")}. ${date.getFullYear()}`,
    };
  });

  // Fetch and group showtimes by format and selected date
  useEffect(() => {
  const fetchShowtimes = async () => {
    if (!movie || !movie.id) return;
    // Llama a tu nuevo endpoint filtrado por movieId
    const res = await fetch(`/api/showtimes/movie/${movie.id}`);
    const showtimesList = await res.json();

    const selectedDay = days[selectedDate].dateObj;
    selectedDay.setHours(0, 0, 0, 0);

    const grouped = { "2D": [], "3D": [], "XD": [] };

    showtimesList.forEach((st) => {
      if (!st.format) return;
      const formatValue = String(st.format).toUpperCase();
      let format = "";
      if (formatValue === "TWO_D" || formatValue === "2D") format = "2D";
      else if (formatValue === "THREE_D" || formatValue === "3D") format = "3D";
      else if (formatValue === "XD" || formatValue === "IMAX" || formatValue === "FOUR_D_X") format = "XD";
      else return;

      // showDate: LocalDate (YYYY-MM-DD), showTime: LocalTime (HH:mm:ss)
      if (!st.showDate || !st.showTime) return;
      const showDate = new Date(st.showDate + 'T' + st.showTime);
      const showDateMidnight = new Date(showDate);
      showDateMidnight.setHours(0, 0, 0, 0);

      if (showDateMidnight.getTime() === selectedDay.getTime()) {
        grouped[format].push({
          id: st.id,
          time: st.showTime.slice(0, 5),
          full: st.showDate + 'T' + st.showTime,
        });
      }
    });

    setGroupedShowtimes(grouped);
  };

  if (movie && movie.id) {
    fetchShowtimes();
  }
  // eslint-disable-next-line
}, [movie, selectedDate]);

  // Handles navigation to the purchase page when selecting a showtime
  const handleSelectShowtime = (format, time) => {
    navigate(`/purchase/${movie.id}/${time}/${format}`);
  };

  if (loading) return <h2 style={{ color: "white" }}>Loading...</h2>;
  if (!movie) return <h2 style={{ color: "white" }}>Movie not found</h2>;

  return (
    <div className="movie-detail-container">
      <div className="left-column">
        <img src={movie.imageUrl} alt={movie.title} className="movie-image" />
        <h3 className="sinopsis-title">Sinopsis</h3>
        <p className="sinopsis-text">{movie.descriptionMovie}</p>
      </div>

      <div className="right-column">
        <h1 className="movie-title">{movie.title}</h1>
        <p>
          <strong>Duración:</strong> {movie.duration} min
        </p>
        <p>
          <strong>Género:</strong> {movie.genreName}
        </p>

        <div className="date-selector">
          {days.map((day) => (
            <button
              key={day.id}
              className={`date-box ${selectedDate === day.id ? "active" : ""}`}
              onClick={() => setSelectedDate(day.id)}
            >
              <p style={{ fontWeight: "bold", fontSize: "16px" }}>
                {day.dayName}
              </p>
              <p style={{ fontSize: "14px", color: "#ccc" }}>
                {day.formattedDate}
              </p>
            </button>
          ))}
        </div>

        <div
          className={`showtime-grid ${selectedDate !== null ? "active" : ""}`}
        >
          {["2D", "3D", "XD"].map((format) => (
            <div key={format} className="format-container">
              <h3>{format}</h3>
              <div className="showtime-buttons">
                {groupedShowtimes[format] &&
                groupedShowtimes[format].length > 0 ? (
                  groupedShowtimes[format].map((st) => (
                    <button
                      key={st.id}
                      className="showtime-button"
                      onClick={() => handleSelectShowtime(format, st.time)}
                    >
                      {st.time}
                    </button>
                  ))
                ) : (
                  <p className="no-showtime">No disponible</p>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MovieDetail;
