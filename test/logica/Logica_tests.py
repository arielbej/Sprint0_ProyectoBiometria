
import sys
import os
import sqlite3
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '../..')))
from src.server.Logica import Logica

"""Pruebas unitarias para la clase Logica.
Estas pruebas verifican la funcionalidad de los metodos de la clase Logica."""


"""Setup del test"""
logica = Logica(db_path=":memory:")

logica.cursor.execute("""
    CREATE TABLE IF NOT EXISTS mediciones (
        ID INTEGER PRIMARY KEY,
        ID_GAS INTEGER,
        VALOR TEXT
    )
""")
logica.con.commit()

    
def test_insertar_y_leer_medicion():
    logica.insertar_medicion(1,1,"100")
    logica.insertar_medicion(2,3,"200")
    logica.insertar_medicion(3,4,"300")
    ultima_medicion=logica.get_ultima_medicion()
    assert ultima_medicion == (3,4,"300")

def test_get_ultimas_x_mediciones():
    ultimas_mediciones=logica.get_ultimas_x_mediciones(3)
    assert ultimas_mediciones == [(3,4,"300"),(2,3,"200"),(1,1,"100")]
    
    
def test_cerrar_conexion():
    logica.cerrar_conexion()
    try:
        logica.get_ultima_medicion()
        assert False  # Si no lanza una excepcion, la prueba falla
    except sqlite3.ProgrammingError:
        assert True  # La excepcion es esperada
        



