import { useState } from "react"; // Import useState for managing component state
import { useParams, useNavigate } from "react-router-dom"; // Import hooks for routing
import { useMovies } from "../hooks/useMovies"; // Import custom hook for fetching movie data from API
import "../styles/TicketPurchase.css"; // Import CSS for styling
import SeatSelection from "../components/SeatSelection"; // Import seat selection component

const TicketPurchase = () => {
  const { id, showtime, format } = useParams(); // Extract movie ID, showtime, and format from URL parameters
  const navigate = useNavigate(); // Hook for handling navigation
  const { movies: movie, loading } = useMovies(id); // Fetch movie data using custom hook

  // State to control visibility of ticket selection and seat selection views
  const [showTicketOptions, setShowTicketOptions] = useState(true);
  const [showSeatSelection, setShowSeatSelection] = useState(false);

  // State to manage ticket quantity
  const [ticketCount, setTicketCount] = useState({ general: "", niño: "" });

  // If loading, display a loading message
  if (loading) return <h2 style={{ color: "white" }}>Loading...</h2>;

  // If movie is not found, display an error message
  if (!movie) return <h2 style={{ color: "white" }}>Movie not found</h2>;

  // Handles changes in ticket selection inputs
  const handleChangeTicketCount = (type, value) => {
    setTicketCount({
      ...ticketCount,
      [type]: value === "" ? "" : Math.max(parseInt(value) || 0, 0),
    });
  };

  // Increment ticket count for the specified type
  const increaseTicket = (type) => {
    setTicketCount({ ...ticketCount, [type]: (ticketCount[type] || 0) + 1 });
  };

  // Decrement ticket count for the specified type (ensuring non-negative values)
  const decreaseTicket = (type) => {
    setTicketCount({
      ...ticketCount,
      [type]: Math.max((ticketCount[type] || 0) - 1, 0),
    });
  };

  // Proceed to seat selection if at least one ticket is selected
  const handleContinueToSeats = () => {
    if (ticketCount.general || ticketCount.niño) {
      setShowTicketOptions(false);
      setShowSeatSelection(true);
    }
  };

  // Go back to ticket selection from seat selection
  const handleGoBackToTickets = () => {
    setShowSeatSelection(false);
    setShowTicketOptions(true);
  };

  return (
    <div className="purchase-container">
      {/* Main container for ticket purchase */}
      <button className="back-button" onClick={() => navigate(-1)}>
        Back
      </button>
      {/* Back button */}
      <h1>{movie.title}</h1> {/* Display movie title */}
      <p>
        <strong>Selected time:</strong> {showtime} -{" "}
        <strong>Format:</strong> {format}
      </p>
      {/* Display selected showtime and format */}
      {/* Ticket selection section */}
      {showTicketOptions && (
        <div className="ticket-options">
          <h3>Select ticket quantity</h3>
          <div className="ticket-inputs">
            <label>
              {/* General ticket selection */}
              General - S/15
              <div className="ticket-counter">
                <button
                  className="counter-button"
                  onClick={() => decreaseTicket("general")}
                >
                  -
                </button>
                <input
                  type="number"
                  min="0"
                  value={ticketCount.general}
                  className="ticket-input"
                  placeholder="Quantity"
                  onChange={(e) =>
                    handleChangeTicketCount("general", e.target.value)
                  }
                />
                <button
                  className="counter-button"
                  onClick={() => increaseTicket("general")}
                >
                  +
                </button>
              </div>
            </label>

            <label>
              {/* Child ticket selection */}
              Niño - S/10
              <div className="ticket-counter">
                <button
                  className="counter-button"
                  onClick={() => decreaseTicket("niño")}
                >
                  -
                </button>
                <input
                  type="number"
                  min="0"
                  value={ticketCount.niño}
                  className="ticket-input"
                  placeholder="Quantity"
                  onChange={(e) =>
                    handleChangeTicketCount("niño", e.target.value)
                  }
                />
                <button
                  className="counter-button"
                  onClick={() => increaseTicket("niño")}
                >
                  +
                </button>
              </div>
            </label>
          </div>

          {/* Summary of selected tickets */}
          {(ticketCount.general || ticketCount.niño) && (
            <div className="summary">
              <h4>Purchase summary:</h4>
              <ul>
                {ticketCount.general && (
                  <li>{ticketCount.general} General ticket(s)</li>
                )}
                {ticketCount.niño && <li>{ticketCount.niño} Child ticket(s)</li>}
              </ul>
              <h3>
                Total: S/
                {15 * (ticketCount.general || 0) + 10 * (ticketCount.niño || 0)}
              </h3>
              <button
                className="confirm-button"
                onClick={handleContinueToSeats}
              >
                Continue
              </button>
            </div>
          )}
        </div>
      )}
      {/* Seat selection section */}
      {showSeatSelection && (
        <div className="seat-selection-container">
          <SeatSelection
            onProceedToPayment={(selectedSeats) => {
              // Navegate to PaymentConfirmation with the necesary data 
              const params = new URLSearchParams({
                seats: selectedSeats.join(","),
                movie: movie.title,
                showtime,
                format,
                ticketsGeneral: ticketCount.general || 0,
                ticketsChild: ticketCount.niño || 0,
              });
              navigate(`/payment-confirmation?${params.toString()}`);
            }}
            ticketCount={ticketCount}
            onGoBack={handleGoBackToTickets}
          />
        </div>
      )}
    </div>
  );
};

export default TicketPurchase; // Export component for use in other parts of the application