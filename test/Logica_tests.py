
import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from src.server.Logica import Logica
import sqlite3

"""Pruebas unitarias para la clase Logica.
Estas pruebas verifican la funcionalidad de los metodos de la clase Logica."""

logica=Logica()

def test_insertar_medicion():
    logica.insertar_medicion(4, 67)
    medicion = logica.get_medicion(4)
    assert medicion == (4, 67)
    
def test_get_medicion():
    medicion = logica.get_medicion(4)
    assert medicion == (4, 67)
    
    
def test_cerrar_conexion():
    logica.cerrar_conexion()
    try:
        logica.get_medicion(4)
        assert False  # Si no lanza una excepcion, la prueba falla
    except sqlite3.ProgrammingError:
        assert True  # La excepcion es esperada
        



