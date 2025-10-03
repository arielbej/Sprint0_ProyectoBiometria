/********************************************
 * @file LogicaFake.js
 * @brief Cliente que llama a la API REST en Python usando fetch
 *******************************************/
class LogicaFake {
  constructor(baseUrl) {
    this.baseUrl = baseUrl; // ej: "http://127.0.0.1:5000"
  }

  // GET -> obtener medición por ID
  async getMedicion(id) {
    const response = await fetch(`${this.baseUrl}/get/${id}`);
    if (!response.ok) {
      throw new Error(`Error GET: ${response.status}`);
    }
    return response.json();
  }

  // GET -> obtener última medición
  async getUltimaMedicion() {
    const response = await fetch(`${this.baseUrl}/ultima`);
    if (!response.ok) {
      throw new Error(`Error GET última: ${response.status}`);
    }
    return response.json();
  }
}

