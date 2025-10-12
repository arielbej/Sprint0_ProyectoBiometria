/********************************************
 * @file mediciones.js
 * @brief Script que carga las últimas mediciones y las muestra en la tabla HTML
 *******************************************/

import { LogicaFake } from "./LogicaFake.js";
const API_URL = "http://192.168.18.199:8000"
const logica = new LogicaFake(API_URL);

// Convierte el id del sensor a texto para mejor legibilidad
// Z(id) --> id_to_str--> str
function id_to_str(id) {
  if (id==11){return "CO2";}
  if (id==12){return "Temperatura";}
  if (id==13){return "Ruido";}
  return "Desconocido";
}

// Z ---> cargarMediciones()
// Esta función obtiene las últimas mediciones desde el backend y las muestra en la tabla HTML
async function cargarMediciones(ultimas_mediciones) {
  try {
    let data = await logica.getMediciones(ultimas_mediciones); // Obtener las últimas 10 mediciones por ejemplo
    console.log("Mediciones obtenidas:", data);

    let tbody = document.getElementById("tableBody");
    tbody.innerHTML = ""; // limpia la tabla antes de llenarla

    if (data.mediciones) {
      data.mediciones.forEach(medicion => {
        let fila = document.createElement("tr");

        fila.innerHTML = `
          <td>${medicion.id}</td>
          <td>${id_to_str(medicion.id_sensor)}</td>
          <td>${medicion.valor_medida}</td>
          <td>${medicion.contador}</td>
        `;

        tbody.appendChild(fila);
      });
    } else {
      tbody.innerHTML = "<tr><td colspan='3'>No hay mediciones</td></tr>";
    }
  } catch (error) {
    console.error("Error al cargar mediciones:", error);
  }
}
// Llama la función al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  cargarMediciones(3);
});

