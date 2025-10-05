from fastapi import FastAPI
from server.bbd.Logica import Logica
from pydantic import BaseModel


# Inicializa la aplicacion FastAPI y la logica de negocio.
app = FastAPI()
logica = Logica()

class Medicion(BaseModel):
    # usamos basemodel para que en el request se valide como json.
    # De la manera anterior (id_gas:int, valor:str) habria que mandar todo como query.
    #Entonces FastAPI espera que esos parámetros se pasen como query parameters o form-data, no como JSON en el body.
    id_sensor: int
    valor_contador: int

# TELEFONO --> API --> LOGICA --> BBD
@app.post("/mediciones")
def insertar_medicion(medicion: Medicion):
    """insertar_medicion
    Endpoint para insertar una medicion en la base de datos.
    
    Args:
        medicion (Medicion): objeto Medicion con los datos a insertar.
    
    Returns:
        dict: diccionario con el mensaje de exito.
    """
    logica.insertar_medicion(medicion.id_sensor, medicion.valor_contador)
    return {"mensaje": "Medicion insertada correctamente"}

# GET → obtener la última medición
@app.get("/mediciones/ultima_medicion_obtenida")
def ultima():
    medicion = logica.get_ultima_medicion()
    if medicion:
        return {"id": medicion[0], "id_sensor": medicion[1], "valor_contador": medicion[2]}
    return {"mensaje": "No hay mediciones"}

# GET → obtener la última medición
@app.get("/mediciones/ultimas_mediciones")
def ultimas(cuantas: int):
    mediciones = logica.get_ultimas_x_mediciones(cuantas)
    if mediciones:
        return {"mediciones": [{"id": m[0], "id_sensor": m[1], "valor_contador": m[2]} for m in mediciones]}
    return {"mensaje": "No hay mediciones"}