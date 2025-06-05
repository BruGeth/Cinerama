import React, { useState } from "react";
import { useParams } from "react-router-dom";
import { MOVIES } from "../Components/movieData";
import "../styles/TicketPurchase.css";
import SeatSelection from "../Components/SeatSelection";

const TicketPurchase = () => {
    const { id, showtime } = useParams();
    const movie = MOVIES.find((m) => m.id === Number(id));

    const [selectedRoom, setSelectedRoom] = useState(null);
    const [showTicketOptions, setShowTicketOptions] = useState(true);
    const [showSeatSelection, setShowSeatSelection] = useState(false);
    const [ticketCount, setTicketCount] = useState({ general: "", niño: "" });

    if (!movie) return <h2 style={{ color: "white" }}>Película no encontrada</h2>;

    const PRICES = {
        "2D": { general: 15, niño: 10 },
        "3D": { general: 18, niño: 12 },
        "XD": { general: 20, niño: 14 }
    };

    const handleSelectRoom = (roomType) => {
        setSelectedRoom(roomType);
    };

    const handleChangeTicketCount = (type, value) => {
        setTicketCount({ ...ticketCount, [type]: parseInt(value) || 0 });
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
            <h1>{movie.title}</h1>
            <h3>Selecciona tu tipo de sala</h3>

            <div className="room-options">
                {Object.keys(PRICES).map((room) => (
                    <button key={room} className="room-button" onClick={() => handleSelectRoom(room)}>
                        {room}
                    </button>
                ))}
            </div>

            <p><strong>Hora seleccionada:</strong> {showtime}</p>

            {showTicketOptions && selectedRoom && (
                <div className="ticket-options">
                    <h3>Selecciona cantidad de entradas - Sala {selectedRoom}</h3>
                    <div className="ticket-inputs">
                        <label>
                            General - S/{PRICES[selectedRoom].general}
                            <input
                                type="number"
                                min="0"
                                value={ticketCount.general}
                                placeholder="Cantidad"
                                onChange={(e) => handleChangeTicketCount("general", e.target.value)}
                            />
                        </label>

                        <label>
                            Niño - S/{PRICES[selectedRoom].niño}
                            <input
                                type="number"
                                min="0"
                                value={ticketCount.niño}
                                placeholder="Cantidad"
                                onChange={(e) => handleChangeTicketCount("niño", e.target.value)}
                            />
                        </label>
                    </div>

                    {(ticketCount.general || ticketCount.niño) && (
                        <div className="summary">
                            <h4>Resumen de compra:</h4>
                            <ul>
                                {ticketCount.general && <li>{ticketCount.general} boleto(s) General</li>}
                                {ticketCount.niño && <li>{ticketCount.niño} boleto(s) Niño</li>}
                            </ul>
                            <h3>Total: S/{PRICES[selectedRoom].general * (ticketCount.general || 0) + PRICES[selectedRoom].niño * (ticketCount.niño || 0)}</h3>
                            <button className="confirm-button" onClick={handleContinueToSeats}>
                                Continuar
                            </button>
                        </div>
                    )}
                </div>
            )}

            {showSeatSelection && (
                <SeatSelection
                    onConfirmSeats={(seats) => console.log(seats)}
                    ticketCount={ticketCount}
                    onGoBack={handleGoBackToTickets}
                />
            )}
        </div>
    );
};

export default TicketPurchase;
