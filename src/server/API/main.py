from fastapi import FastAPI
from server.bbd.Logica import Logica
from pydantic import BaseModel
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles
import os

#-----------------------------------------------------------------------------API-----------------------------------------------------------------------------
# Inicializa la aplicacion FastAPI y la logica de negocio.
app = FastAPI()
logica = Logica()

class Medicion(BaseModel):
    # usamos basemodel para que en el request se valide como json.
    # De la manera anterior (id_gas:int, valor:str) habria que mandar todo como query.
    #Entonces FastAPI espera que esos parámetros se pasen como query parameters o form-data, no como JSON en el body.
    id_sensor: int
    valor_medida: int
    contador: int

# TELEFONO --> API --> LOGICA --> BBD
@app.post("/mediciones")
def insertar_medicion(medicion: Medicion):
    """insertar_medicion
    Endpoint para insertar una medicion en la base de datos.
    Medicion --> insertar_medicion 
    
    Args:
        medicion (Medicion): objeto Medicion con los datos a insertar.
    
    Returns:
        dict: diccionario con el mensaje de exito.
    """
    logica.insertar_medicion(medicion.id_sensor, medicion.valor_medida, medicion.contador)
    return {"mensaje": "Medicion insertada correctamente"}
#//()

# GET → obtener la última medición
@app.get("/mediciones/ultima_medicion_obtenida")
def get_ultima_medicion():
    """get_ultima_medicion
    Devuelve la ultima medida que esta en la bbd.
    dict con mediciones<---- get_ultima_medicion()

    Returns:
        dict: diccionario con los datos de la ultima medicion o mensaje si no hay mediciones.
    """
    medicion = logica.get_ultima_medicion()
    if medicion:
        return {"id": medicion[0], "id_sensor": medicion[1], "valor_medida": medicion[2], "contador": medicion[3]}
    return {"mensaje": "No hay mediciones"}

# GET → obtener la última medición
@app.get("/mediciones/ultimas_mediciones")
def get_ultimas_mediciones(cuantas: int):
    """get_ultimas_mediciones
    Devuelve las ultimas X medidas que estan en la bbd.
    Z                           ----> 
    lista<dict con mediciones> <---- get_ultima_medicion()


    Args:
        cuantas (int): cuantas mediciones quieres

    Returns:
        dict: diccionario con los datos de las ultimas mediciones o mensaje si no hay mediciones.
    """
    mediciones = logica.get_ultimas_x_mediciones(cuantas)
    if mediciones:
        return {"mediciones": [{"id": m[0], "id_sensor": m[1], "valor_medida": m[2],"contador":m[3]} for m in mediciones]}
    return {"mensaje": "No hay mediciones"}

#-----------------------------------------------------------------------------API-----------------------------------------------------------------------------



#---------------------------------------------------------------------------------------WEB-----------------------------------------------------------------------------#
# Ruta absoluta de la carpeta 'web'
BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
WEB_DIR = os.path.join(BASE_DIR, "web")
INDEX_FILE = os.path.join(WEB_DIR, "index.html")    

# Montar los archivos estáticos (css, js, etc.)
app.mount("/static", StaticFiles(directory=WEB_DIR), name="static") 

# Todo mesto es para levantar la web a la vez que la api

@app.get("/")
def read_index():
    """read_index
    Endpoint para servir el archivo index.html.
    FileResponse <---- read_index()
    
    Returns:
        FileResponse: respuesta con el archivo index.html.
    """
    return FileResponse(INDEX_FILE)
#---------------------------------------------------------------------------------------WEB-----------------------------------------------------------------------------#
