package com.cinerama.backend.dto;

import lombok.Data;

/**
 * Representa una solicitud para reservar una función especial en el sistema Cinerama.
 *
 * <p>Esta clase define la estructura de los datos necesarios para procesar una
 * solicitud de función especial. Incluye detalles sobre la función, el cine y
 * la información de contacto.</p>
 *
 * <h2>Campos:</h2>
 * <ul>
 *   <li><b>cinema:</b> Nombre del cine donde se llevará a cabo la función (requerido)</li>
 *   <li><b>movie:</b> Película seleccionada para la función especial (requerido)</li>
 *   <li><b>date:</b> Fecha de la función en formato YYYY-MM-DD (requerido)</li>
 *   <li><b>time:</b> Hora de la función en formato HH:mm (requerido)</li>
 *   <li><b>capacity:</b> Número de asistentes esperados (mínimo: 1)</li>
 *   <li><b>requirements:</b> Requisitos específicos para la función (opcional)</li>
 *   <li><b>contactName:</b> Nombre de la persona de contacto (requerido)</li>
 *   <li><b>contactEmail:</b> Correo electrónico de la persona de contacto (requerido, debe ser válido)</li>
 *   <li><b>contactPhone:</b> Teléfono de la persona de contacto (requerido)</li>
 *   <li><b>company:</b> Empresa asociada con la solicitud (opcional)</li>
 *   <li><b>message:</b> Mensaje o información adicional sobre la solicitud (opcional)</li>
 * </ul>
 *
 * <h2>Validación:</h2>
 * <p>Los campos marcados como requeridos deben ser proporcionados y cumplir con las
 * restricciones especificadas. Por ejemplo, el correo debe tener un formato válido
 * y el número de asistentes debe ser al menos 1.</p>
 *
 * <h2>Uso:</h2>
 * <p>Este DTO se utiliza en el `SpecialFunctionController` para manejar las solicitudes
 * de reserva de funciones especiales.</p>
 *
 * @see com.cinerama.backend.controller.SpecialFunctionController
 * @see com.cinerama.backend.service.SpecialFunctionService
 */

@Data
public class SpecialFunctionRequest {

    /* === Name of the cinema where the function will take place === */
    private String cinema;
    /* === Movie selected for the special function === */
    private String movie;
    /* === Details of the special function === */
    private String date;
    /* === Time of the special function === */
    private String time;
    /* === Number of attendees for the special function === */
    private Integer capacity;
    /* === Specific requirements for the special function === */
    private String requirements;
    /* === Name of the contact person for the request === */
    private String contactName;
    /* === Email of the contact person === */
    private String contactEmail;
    /* === Phone number of the contact person === */
    private String contactPhone;
    /* === Company associated with the special function request === */
    private String company;
    /* === Message or additional information regarding the special function request === */
    private String message;
}
