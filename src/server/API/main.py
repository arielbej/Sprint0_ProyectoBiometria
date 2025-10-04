from fastapi import FastAPI
from ..Logica import Logica

# Inicializa la aplicacion FastAPI y la logica de negocio.
app = FastAPI()
logica = Logica()

# TELEFONO --> API --> LOGICA --> BBD
@app.post("/mediciones")
def insertar_medicion(id_gas: int, valor: str):
    """insertar_medicion
    Endpoint para insertar una medicion en la base de datos.
    
    Args:
        id_gas (int): numero id del gas.
        valor (string): el valor de la medidada en formato json({"temperatura":35,"nivel_gas":30%} por ejemplo).
    
    Returns:
        dict: diccionario con el mensaje de exito.
    """
    logica.insertar_medicion(id_gas,valor)
    return {"mensaje": "Medicion insertada correctamente"}

# GET → obtener la última medición
@app.get("/mediciones/ultima_medicion_obtenida")
def ultima():
    medicion = logica.get_ultima_medicion()
    if medicion:
        return {"id": medicion[0], "id_gas": medicion[1], "valor": medicion[2]}
    return {"mensaje": "No hay mediciones"}

# GET → obtener la última medición
@app.get("/mediciones/ultimas_mediciones")
def ultimas(cuantas: int):
    mediciones = logica.get_ultimas_x_mediciones(cuantas)
    if mediciones:
        return {"mediciones": [{"id": m[0], "id_gas": m[1], "valor": m[2]} for m in mediciones]}
    return {"mensaje": "No hay mediciones"}