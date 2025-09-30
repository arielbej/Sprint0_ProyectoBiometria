# Logica.py
# Clase que define la logica de negocio de la aplicacion.
import os
import sqlite3

class Logica:
    def __init__(self):
        db_folder = 'src/server/bbd'
        db_path = os.path.join(db_folder, 'mediciones.db')
        # Ensure the folder exists
        os.makedirs(db_folder, exist_ok=True)
        con = sqlite3.connect(db_path)
        self.con=con
        self.cursor=con.cursor()

    def insertar_medicion(self, id,medicion):
        # Inserta una nueva medicion en la base de datos.
        self.cursor.execute("INSERT INTO mediciones (id, medicion) VALUES (?, ?)", (id, medicion))
        self.con.commit()
        
    def get_medicion(self,id):
        # Obtiene una medicion de la base de datos por su id.
        self.cursor.execute("SELECT * FROM mediciones WHERE id=?", (id,))#nota, funciona solo con tuplas(segundo parametro)
        return self.cursor.fetchone()
        
        
    def cerrar_conexion(self):
        # Cierra la conexion a la base de datos.
        self.con.close()
        
        
