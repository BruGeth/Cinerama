import React, { useState } from "react"; // Import React and useState hook
import "../styles/SeatSelection.css"; // Import CSS for styling

const SeatSelection = ({ onProceedToPayment, ticketCount, onGoBack }) => {
  // Define rows using uppercase letters and columns numbered from 1 to 20
  const rows = "ABCDEFGHIJKLMNOPQ".split(""); 
  const columns = Array.from({ length: 20 }, (_, i) => i + 1); 

  const [selectedSeats, setSelectedSeats] = useState([]); // State to store selected seats

  // Calculate the total number of tickets
  const totalTickets = parseInt(ticketCount.general || 0) + parseInt(ticketCount.niño || 0);

  // Set of predefined empty seats that are unavailable for selection
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

  // Function to toggle seat selection
  const toggleSeatSelection = (seat) => {
    if (selectedSeats.includes(seat)) {
      setSelectedSeats(selectedSeats.filter((s) => s !== seat)); // Remove seat if already selected
    } else if (selectedSeats.length < totalTickets) {
      setSelectedSeats([...selectedSeats, seat]); // Add seat if selection limit not reached
    }
  };

  return (
    <div className="seat-selection-container">
      <h2>Selecciona tus asientos</h2>
      <p>Total de boletos: {totalTickets}</p>
      <p>Asientos seleccionados: {selectedSeats.length} / {totalTickets}</p>

      <div className="cinema-container">
        <div className="cinema-screen">Pantalla</div> {/* Display screen section */}
        <div className="seat-grid"> {/* Render seat grid dynamically */}
          {rows.map((row) => (
            <div key={row} className="seat-row"> {/* Render each row */}
              {columns.map((col) => {
                const seat = `${row}${col}`; // Generate seat identifier
                return emptySeats.has(seat) ? ( 
                  <div key={seat} className="empty-space"></div> // Render empty spaces for unavailable seats
                ) : (
                  <button
                    key={seat}
                    className={`seat ${selectedSeats.includes(seat) ? "selected" : ""}`} // Apply selected styling
                    onClick={() => toggleSeatSelection(seat)} // Handle seat selection
                    disabled={selectedSeats.length >= totalTickets && !selectedSeats.includes(seat)} // Disable button if max selection reached
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
        <button className="payment-button" onClick={onProceedToPayment}> {/* Proceed to payment button */}
          Continuar con pago
        </button>
      </div>
    </div>
  );
};

export default SeatSelection; // Export component for use in other parts of the application
