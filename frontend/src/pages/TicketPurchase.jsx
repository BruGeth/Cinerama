import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { movies } from "../components/DataMovie";
import "../styles/TicketPurchase.css";
import SeatSelection from "../components/SeatSelection";

const TicketPurchase = () => {
    const { id, showtime, format } = useParams(); // Get movie ID, showtime, and format from URL
    const movie = movies.find((m) => m.id === Number(id)); // Find selected movie
    const navigate = useNavigate(); // Navigation hook

    // State control for ticket and seat selection views
    const [showTicketOptions, setShowTicketOptions] = useState(true);
    const [showSeatSelection, setShowSeatSelection] = useState(false);

    // State for ticket quantity management
    const [ticketCount, setTicketCount] = useState({ general: "", child: "" });

    if (!movie) return <h2 style={{ color: "white" }}>Movie not found</h2>; // Error message if no movie is found

    // Handle ticket quantity changes
    const handleChangeTicketCount = (type, value) => {
        setTicketCount({ ...ticketCount, [type]: value === "" ? "" : Math.max(parseInt(value) || 0, 0) });
    };

    const increaseTicket = (type) => setTicketCount({ ...ticketCount, [type]: (ticketCount[type] || 0) + 1 });
    const decreaseTicket = (type) => setTicketCount({ ...ticketCount, [type]: Math.max((ticketCount[type] || 0) - 1, 0) });

    // Proceed to seat selection if at least one ticket is selected
    const handleContinueToSeats = () => {
        if (ticketCount.general || ticketCount.child) {
            setShowTicketOptions(false);
            setShowSeatSelection(true);
        }
    };

    // Navigate back to ticket selection
    const handleGoBackToTickets = () => {
        setShowSeatSelection(false);
        setShowTicketOptions(true);
    };

    // Navigate to payment confirmation, passing ticket details
    const handleContinueToPayment = (selectedSeats) => {
        if (!Array.isArray(selectedSeats)) {
            console.error("Error: selectedSeats is not an array", selectedSeats);
            return;
        }
        navigate(`/payment-confirmation?movie=${movie.title}&showtime=${showtime}&format=${format}&ticketsGeneral=${ticketCount.general}&ticketsChild=${ticketCount.child}&seats=${selectedSeats.join(",")}`);
    };

    return (
        <div className="purchase-container"> {/* Main container */}
            <button className="back-button" onClick={() => navigate(-1)}>Back</button> {/* Back button */}
            <h1>{movie.title}</h1> {/* Movie title */}
            <p><strong>Selected Time:</strong> {showtime} - <strong>Format:</strong> {format}</p> {/* Showtime and format */}

            {/* Ticket selection section */}
            {showTicketOptions && (
                <div className="ticket-options">
                    <h3>Select ticket quantity</h3>
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
                                    placeholder="Quantity"
                                    onChange={(e) => handleChangeTicketCount("general", e.target.value)}
                                />
                                <button className="counter-button" onClick={() => increaseTicket("general")}>+</button>
                            </div>
                        </label>

                        <label> {/* Child ticket selection */}
                            Child - S/10
                            <div className="ticket-counter">
                                <button className="counter-button" onClick={() => decreaseTicket("child")}>-</button>
                                <input
                                    type="number"
                                    min="0"
                                    value={ticketCount.child}
                                    className="ticket-input"
                                    placeholder="Quantity"
                                    onChange={(e) => handleChangeTicketCount("child", e.target.value)}
                                />
                                <button className="counter-button" onClick={() => increaseTicket("child")}>+</button>
                            </div>
                        </label>
                    </div>

                    {/* Purchase summary */}
                    {(ticketCount.general || ticketCount.child) && (
                        <div className="summary">
                            <h4>Purchase Summary:</h4>
                            <ul>
                                {ticketCount.general && <li>{ticketCount.general} Ticket General(s)</li>}
                                {ticketCount.child && <li>{ticketCount.child} Ticket para niño(s)</li>}
                            </ul>
                            <h3>Total: S/{15 * (ticketCount.general || 0) + 10 * (ticketCount.child || 0)}</h3>
                            <button className="confirm-button" onClick={handleContinueToSeats}>
                                Continuar
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
