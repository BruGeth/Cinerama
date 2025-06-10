import React, { useState } from "react"; // Import React and useState for managing component state
import { useParams, useNavigate } from "react-router-dom"; // Import hooks for routing
import { movies } from "../components/DataMovie"; // Import movie data
import "../styles/TicketPurchase.css"; // Import CSS for styling
import SeatSelection from "../components/SeatSelection"; // Import seat selection component

const TicketPurchase = () => {
    const { id, showtime, format } = useParams(); // Extract movie ID, showtime, and format from URL parameters
    const movie = movies.find((m) => m.id === Number(id)); // Find movie details based on ID
    const navigate = useNavigate(); // Hook for handling navigation

    // State to control visibility of ticket selection and seat selection views
    const [showTicketOptions, setShowTicketOptions] = useState(true);
    const [showSeatSelection, setShowSeatSelection] = useState(false);

    // State to manage ticket quantity
    const [ticketCount, setTicketCount] = useState({ general: "", niño: "" });

    // If movie is not found, display an error message
    if (!movie) return <h2 style={{ color: "white" }}>Película no encontrada</h2>;

    // Handles changes in ticket selection inputs
    const handleChangeTicketCount = (type, value) => {
        setTicketCount({ ...ticketCount, [type]: value === "" ? "" : Math.max(parseInt(value) || 0, 0) });
    };

    // Increment ticket count for the specified type
    const increaseTicket = (type) => {
        setTicketCount({ ...ticketCount, [type]: (ticketCount[type] || 0) + 1 });
    };

    // Decrement ticket count for the specified type (ensuring non-negative values)
    const decreaseTicket = (type) => {
        setTicketCount({ ...ticketCount, [type]: Math.max((ticketCount[type] || 0) - 1, 0) });
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
    // Navigate to the payment confirmation page, passing the selected details in the URL parameters
    const handleContinueToPayment = (selectedSeats) => {
        if (!Array.isArray(selectedSeats)) {
            console.error("Error: selectedSeats is not an array", selectedSeats);
            return;
        }
        navigate(`/payment-confirmation?movie=${movie.title}&showtime=${showtime}&format=${format}&ticketsGeneral=${ticketCount.general}&ticketsChild=${ticketCount.niño}&seats=${selectedSeats.join(",")}`);
    };

    return (
        <div className="purchase-container"> {/* Main container for ticket purchase */}
            <button className="back-button" onClick={() => navigate(-1)}>Volver</button> {/* Back button */}
            <h1>{movie.title}</h1> {/* Display movie title */}
            <p><strong>Hora seleccionada:</strong> {showtime} - <strong>Formato:</strong> {format}</p> {/* Display selected showtime and format */}

            {/* Ticket selection section */}
            {showTicketOptions && (
                <div className="ticket-options">
                    <h3>Selecciona cantidad de entradas</h3>
                    <div className="ticket-inputs">
                        <label> {/* General ticket selection */}
                            General - S/15
                            <div className="ticket-counter">
                                <button className="counter-button" onClick={() => decreaseTicket("general")}>-</button>
                                <input
                                    type="number"
                                    min="0"
                                    value={ticketCount.general}
                                    className="ticket-input"
                                    placeholder="Cantidad"
                                    onChange={(e) => handleChangeTicketCount("general", e.target.value)}
                                />
                                <button className="counter-button" onClick={() => increaseTicket("general")}>+</button>
                            </div>
                        </label>

                        <label> {/* Niño ticket selection */}
                            Niño - S/10
                            <div className="ticket-counter">
                                <button className="counter-button" onClick={() => decreaseTicket("niño")}>-</button>
                                <input
                                    type="number"
                                    min="0"
                                    value={ticketCount.niño}
                                    className="ticket-input"
                                    placeholder="Cantidad"
                                    onChange={(e) => handleChangeTicketCount("niño", e.target.value)}
                                />
                                <button className="counter-button" onClick={() => increaseTicket("niño")}>+</button>
                            </div>
                        </label>
                    </div>

                    {/* Summary of selected tickets */}
                    {(ticketCount.general || ticketCount.niño) && (
                        <div className="summary">
                            <h4>Resumen de compra:</h4>
                            <ul>
                                {ticketCount.general && <li>{ticketCount.general} boleto(s) General</li>}
                                {ticketCount.niño && <li>{ticketCount.niño} boleto(s) Niño</li>}
                            </ul>
                            <h3>Total: S/{15 * (ticketCount.general || 0) + 10 * (ticketCount.niño || 0)}</h3>
                            <button className="confirm-button" onClick={handleContinueToSeats}>
                                Continue to Seat Selection
                            </button>
                        </div>
                    )}
                </div>
            )}

            {/* Seat selection section */}
            {showSeatSelection && (
                <div className="seat-selection-container">
                    <SeatSelection
                        onProceedToPayment={(seats) => handleContinueToPayment(seats)}
                        ticketCount={ticketCount}
                        onGoBack={handleGoBackToTickets}
                    />
                </div>
            )}
        </div>
    );
};

export default TicketPurchase; // Export component for use in other parts of the application
