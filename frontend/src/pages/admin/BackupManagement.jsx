import React, { useState } from "react";
import "../../styles/BackupManagement.css";
import axios from "axios";
import AdminLayout from "../../layouts/AdminLayout";

const BackupManagement = () => {
  const [statusMessage, setStatusMessage] = useState("");
  const [backupFile, setBackupFile] = useState(null);
  const [showBackupModal, setShowBackupModal] = useState(false);
  const [showConfigFormModal, setShowConfigFormModal] = useState(false);

  const [config, setConfig] = useState({
    folderPath: "C:\\Users\\Usuario\\Desktop\\Cinerama\\backend\\backups",
    fileName: "backup-cinerama.sql",
    autoTime: "04:30"
  });

  const handleConfirmBackup = () => {
    const token = localStorage.getItem("token");

    axios
      .post("/api/admin/backup", config, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      .then((res) => {
        setStatusMessage(res.data.message);
        setBackupFile(res.data.fileName);
        setShowBackupModal(false);
      })
      .catch((err) => {
        console.error(err);
        setStatusMessage("❌ Error al generar respaldo.");
        setBackupFile(null);
        setShowBackupModal(false);
      });
  };

  const handleSaveConfig = () => {
  const token = localStorage.getItem("token");

  // ⚠️ Usar hora actual solo si autoTime está vacío
  let updatedAutoTime = config.autoTime;
  if (!updatedAutoTime || updatedAutoTime.trim() === "") {
    const now = new Date();
    const hours = now.getHours().toString().padStart(2, "0");
    const minutes = now.getMinutes().toString().padStart(2, "0");
    updatedAutoTime = `${hours}:${minutes}`;
  }

  const updatedConfig = {
    ...config,
    autoTime: updatedAutoTime
  };

  setConfig(updatedConfig);

  axios
    .post("/api/admin/config/backup", updatedConfig, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(() => {
      setStatusMessage("✅ Configuración actualizada correctamente.");
      setShowConfigFormModal(false);
    })
    .catch((err) => {
      console.error(err);
      setStatusMessage("❌ Error al guardar la configuración.");
      setShowConfigFormModal(false);
    });
};

  const handleChange = (e) => {
    const { name, value } = e.target;
    setConfig({ ...config, [name]: value });
  };

  return (
    <AdminLayout>
      <div className="backup-section">
        <h2>Respaldo del Sistema</h2>
        <p>El sistema realiza respaldos de la base de datos del cine. Aquí puedes ejecutarlo manualmente o configurar opciones como ruta, nombre y horario.</p>

        <div className="backup-actions">
          <button className="backup-btn primary" onClick={() => setShowBackupModal(true)}>
            <span className="backup-icon">💾</span>
            Generar Respaldo
          </button>

          <button className="backup-btn secondary" onClick={() => setShowConfigFormModal(true)}>
            <span className="backup-icon">⚙️</span>
            Configuración
          </button>
        </div>

        {statusMessage && (
          <div className={`status-message ${statusMessage.includes("Error") ? "error" : ""}`}>
            {statusMessage}
          </div>
        )}

        {backupFile && (
          <div className="backup-path">
            <strong>📂 Ubicación del respaldo:</strong><br />
            {config.folderPath}\\{backupFile}
          </div>
        )}

        {/* Modal de confirmación */}
        {showBackupModal && (
          <div className="mini-modal">
            <div className="mini-modal-box">
              <h4>¿Deseas generar el respaldo?</h4>
              <div className="modal-buttons">
                <button className="modal-confirm" onClick={handleConfirmBackup}>Sí, continuar</button>
                <button className="modal-cancel" onClick={() => setShowBackupModal(false)}>Cancelar</button>
              </div>
            </div>
          </div>
        )}

        {/* Modal de configuración */}
        {showConfigFormModal && (
          <div className="mini-modal">
            <div className="mini-modal-box config-modal">
              <h4>Configuración de Respaldos</h4>
              <form className="config-form">
                <div className="form-row">
                  <label>Ruta de carpeta</label>
                  <input
                    type="text"
                    name="folderPath"
                    value={config.folderPath}
                    onChange={handleChange}
                  />
                </div>
                <div className="form-row">
                  <label>Nombre del archivo</label>
                  <input
                    type="text"
                    name="fileName"
                    value={config.fileName}
                    onChange={handleChange}
                  />
                </div>
                <div className="form-row">
                  <label>Hora automática</label>
                  <input
                    type="time"
                    name="autoTime"
                    value={config.autoTime}
                    onChange={handleChange}
                  />
                </div>
              </form>
              <div className="modal-buttons">
                <button className="modal-confirm" onClick={handleSaveConfig}>Guardar</button>
                <button className="modal-cancel" onClick={() => setShowConfigFormModal(false)}>Cancelar</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </AdminLayout>
  );
};

export default BackupManagement;
