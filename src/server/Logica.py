# Logica.py
# Clase que define la logica de negocio de la aplicacion.
import os
import sqlite3

class Logica:
    def __init__(self,db_path="src/server/bbd/mediciones.db"):
        # Asegura que la carpeta exista
        self.con = sqlite3.connect(db_path)
        self.cursor = self.con.cursor()

    def insertar_medicion(self, id,id_gas,valor):
        # Inserta una nueva medicion en la base de datos.
        self.cursor.execute("INSERT INTO mediciones (ID, ID_GAS,VALOR) VALUES (?, ?, ?)", (id,id_gas,valor))
        self.con.commit()
    
    
    def get_ultima_medicion(self):
        # Obtiene la última medición insertada en la tabla
        self.cursor.execute("SELECT * FROM mediciones ORDER BY id DESC LIMIT 1")
        return self.cursor.fetchone()

    
    def get_ultimas_x_mediciones(self,cuantas):
        # Obtiene la últimas x meddiciones de la tabla(x siendo cuantas quiero)
        cuantas=str(cuantas)
        self.cursor.execute(f"SELECT * FROM mediciones ORDER BY id DESC LIMIT {cuantas}")
        return self.cursor.fetchall()
        
        
    def cerrar_conexion(self):
        # Cierra la conexion a la base de datos.
        self.con.close()
        
        
