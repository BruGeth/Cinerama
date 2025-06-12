import { useState, useEffect } from "react";
import axios from "axios";
import AdminLayout from "../../layouts/AdminLayout";
import "../../styles/Users.css";

function Users() {  
  const [users, setUsers] = useState([]);
  const [downloadMessage, setDownloadMessage] = useState("");

   useEffect(() => {
     axios.get("http://localhost:8080/api/user/all", {
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }, // Ensure the token is included in the request headers
     })
     .then(response => setUsers(response.data))
     .catch(error => console.error("Error fetching users:", error));
    }, []);


  const handleDownload = async () => {
  try {
    setDownloadMessage("Generating Excel file...");
    const response = await axios.get("http://localhost:8080/api/user/export", {
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }, // Ensure authentication
      responseType: "blob",
    });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "usuarios.xlsx");
    document.body.appendChild(link);
    link.click();

    setDownloadMessage("Descarga completa ✅");
  } catch (error) {
    setDownloadMessage("Error al generar el archivo ❌");
    console.error("Error al descargar Excel:", error);
  }
};

  return (
    <AdminLayout>
      <div className="users-container">
        <h2>Registro de Usuarios </h2>
        <table className="users-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
            </tr>
          </thead>
          <tbody>
            {users.map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>{typeof user.role === "object" ? user.role.name : user.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <button className="export-btn" onClick={handleDownload}>
          Download Excel
        </button>
        {downloadMessage && <p className="download-message">{downloadMessage}</p>}
      </div>
    </AdminLayout>
  );
}

export default Users;  // Updated export statement