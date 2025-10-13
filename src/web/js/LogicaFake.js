/********************************************
 * @file LogicaFake.js
 * @brief Cliente que llama a la API REST en Python usando fetch
 *******************************************/
class LogicaFake {
  constructor(baseUrl) {
    this.baseUrl = baseUrl; // ej: "http://127.0.0.1:5000"
  }

  // Z--> getMediciones--> T(json)
  async getMediciones(cuantas) {
    let response = await fetch(`${this.baseUrl}/mediciones/ultimas_mediciones?cuantas=${cuantas}`);
    if (!response.ok) {
      throw new Error(`Error GET getMediciones: ${response.status}`);
    }
    return response.json();
  }
}

export { LogicaFake };

