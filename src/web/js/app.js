/********************************************
 * @file app.js
 * @brief Script que carga las últimas mediciones y las muestra en la tabla HTML
 *******************************************/

import { LogicaFake } from "./LogicaFake.js";

const logica = new LogicaFake("http://192.168.18.199:8000");

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
          <td>${medicion.id_gas}</td>
          <td>${medicion.valor}</td>
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
window.onload = cargarMediciones(3); // Cargar las últimas 10 mediciones
