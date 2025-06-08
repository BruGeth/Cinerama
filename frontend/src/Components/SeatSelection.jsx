import React, { useState } from "react";
import "../styles/SeatSelection.css";

const SeatSelection = ({ onProceedToPayment, ticketCount, onGoBack }) => {
  const rows = "ABCDEFGHIJKLMNOPQ".split("");
  const columns = Array.from({ length: 20 }, (_, i) => i + 1);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const totalTickets = parseInt(ticketCount.general || 0) + parseInt(ticketCount.niño || 0);

  const emptySeats = new Set([
    "A4", "A5", "A6", "A15", "A16", "A17",
    "B4", "B5", "B6", "B15", "B16", "B17",
    "C4", "C5", "C6", "C16", "C17",
    "D4", "D5", "D16", "D17",
    "E4", "E16", "E17",
    "F4", "F16", "F17",
    "G4", "G17",
    "H4", "H17",
    "I3", "I4", "I17",
    "J3", "J4", "J17",
    "K3", "K4", "K17",
    "L3", "L4", "L17",
    "M3", "M4", "M17",
    "N3", "N4", "N17",
    "O3", "O4", "O17",
    "P3", "P4", "P17",
    "Q3", "Q4", "Q17",
  ]);

  const toggleSeatSelection = (seat) => {
    if (selectedSeats.includes(seat)) {
      setSelectedSeats(selectedSeats.filter((s) => s !== seat));
    } else if (selectedSeats.length < totalTickets) {
      setSelectedSeats([...selectedSeats, seat]);
    }
  };

  return (
    <div className="seat-selection-container">
      <h2>Selecciona tus asientos</h2>
      <p>Total de boletos: {totalTickets}</p>
      <p>Asientos seleccionados: {selectedSeats.length} / {totalTickets}</p>

      <div className="cinema-container">
        <div className="cinema-screen">Pantalla</div>
        <div className="seat-grid">
          {rows.map((row) => (
            <div key={row} className="seat-row">
              {columns.map((col) => {
                const seat = `${row}${col}`;
                return emptySeats.has(seat) ? (
                  <div key={seat} className="empty-space"></div>
                ) : (
                  <button
                    key={seat}
                    className={`seat ${selectedSeats.includes(seat) ? "selected" : ""}`}
                    onClick={() => toggleSeatSelection(seat)}
                    disabled={selectedSeats.length >= totalTickets && !selectedSeats.includes(seat)}
                  >
                    {seat}
                  </button>
                );
              })}
            </div>
          ))}
        </div>
      </div>

      <div className="button-container">
        <button className="payment-button" onClick={onProceedToPayment}>
          Continuar con pago
        </button>
      </div>
    </div>
  );
};

export default SeatSelection;
