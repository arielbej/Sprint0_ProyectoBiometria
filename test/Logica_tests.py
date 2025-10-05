import sys
import os
import sqlite3
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '../..')))
from server.bbd.Logica import Logica

"""Pruebas unitarias para la clase Logica.
Estas pruebas verifican la funcionalidad de los metodos de la clase Logica."""


"""Setup del test"""
logica = Logica(db_path=":memory:")

logica.cursor.execute("""
    CREATE TABLE IF NOT EXISTS mediciones (
        ID INTEGER PRIMARY KEY AUTOINCREMENT,
        ID_SENSOR INTEGER,
        VALOR_CONTADOR INTEGER
    )
""")
logica.con.commit()

    
def test_insertar_y_leer_medicion():
    """test_insertar_y_leer_medicion
    Inserta varias mediciones y verifica que se leen correctamente.
    """
    logica.insertar_medicion(11, 3)  
    logica.insertar_medicion(12, 4)  
    logica.insertar_medicion(13, 6) 
    ultima_medicion = logica.get_ultima_medicion()
    # La tupla completa es: (ID, ID_SENSOR, VALOR_CONTADOR)
    assert ultima_medicion == (3, 13, 6)  
def test_get_ultimas_x_mediciones():
    """test_get_ultimas_x_mediciones
    prueba la obtencion de las ultimas x mediciones.
    """
    ultimas_mediciones = logica.get_ultimas_x_mediciones(3)
    # Las tuplas son: (ID, ID_GAS, VALOR) en orden descendente por ID
    assert ultimas_mediciones == [(3, 13, 6), (2, 12, 4), (1, 11, 3)]
    
    
def test_cerrar_conexion():
    """test_cerrar_conexion
    Prueba que la conexion se cierra correctamente.
    """
    logica.cerrar_conexion()
    try:
        logica.get_ultima_medicion()
        assert False  # Si no lanza una excepcion, la prueba falla
    except sqlite3.ProgrammingError:
        assert True  # La excepcion es esperada