import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { MOVIES } from "../Components/movieData";
import "../styles/TicketPurchase.css";
import SeatSelection from "../Components/SeatSelection";

const TicketPurchase = () => {
    const { id, showtime, format } = useParams();
    const movie = MOVIES.find((m) => m.id === Number(id));
    const navigate = useNavigate();
    const [showTicketOptions, setShowTicketOptions] = useState(true);
    const [showSeatSelection, setShowSeatSelection] = useState(false);
    const [ticketCount, setTicketCount] = useState({ general: "", niño: "" });

    if (!movie) return <h2 style={{ color: "white" }}>Película no encontrada</h2>;

    const handleChangeTicketCount = (type, value) => {
        setTicketCount({ ...ticketCount, [type]: value === "" ? "" : Math.max(parseInt(value) || 0, 0) });
    };

    const increaseTicket = (type) => {
        setTicketCount({ ...ticketCount, [type]: (ticketCount[type] || 0) + 1 });
    };

    const decreaseTicket = (type) => {
        setTicketCount({ ...ticketCount, [type]: Math.max((ticketCount[type] || 0) - 1, 0) });
    };

    const handleContinueToSeats = () => {
        if (ticketCount.general || ticketCount.niño) {
            setShowTicketOptions(false);
            setShowSeatSelection(true);
        }
    };

    const handleGoBackToTickets = () => {
        setShowSeatSelection(false);
        setShowTicketOptions(true);
    };

    return (
        <div className="purchase-container">
             <button className="back-button" onClick={() => navigate(-1)}>Volver</button>
            <h1>{movie.title}</h1>
            <p><strong>Hora seleccionada:</strong> {showtime} - <strong>Formato:</strong> {format}</p>

            {showTicketOptions && (
                <div className="ticket-options">
                    <h3>Selecciona cantidad de entradas</h3>
                    <div className="ticket-inputs">
                        <label>
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

                        <label>
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

                    {(ticketCount.general || ticketCount.niño) && (
                        <div className="summary">
                            <h4>Resumen de compra:</h4>
                            <ul>
                                {ticketCount.general && <li>{ticketCount.general} boleto(s) General</li>}
                                {ticketCount.niño && <li>{ticketCount.niño} boleto(s) Niño</li>}
                            </ul>
                            <h3>Total: S/{15 * (ticketCount.general || 0) + 10 * (ticketCount.niño || 0)}</h3>
                            <button className="confirm-button" onClick={handleContinueToSeats}>
                                Continuar
                            </button>
                        </div>
                    )}
                </div>
            )}

            {showSeatSelection && (
                <div className="seat-selection-container">
                    <SeatSelection
                        onConfirmSeats={(seats) => console.log(seats)}
                        ticketCount={ticketCount}
                        onGoBack={handleGoBackToTickets}
                    />
 
                </div>
            )}

        </div>
    );
};

export default TicketPurchase;
