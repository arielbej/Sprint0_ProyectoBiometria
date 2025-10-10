import os
import sqlite3

class Logica:
    """Logica
    Clase que define la logica de negocio de la aplicacion.
    Nota: la conexion a la base de datos se abre al crear la instancia de la clase.
    
    Args_constructor: db_path: ruta a la base de datos sqlite (por defecto "src/server/bbd/mediciones.db")
    
    """
    
    def __init__(self, db_path=None):
        if db_path is None:
            # DB por defecto -> siempre relativa a este archivo
            base_dir = os.path.dirname(os.path.abspath(__file__))
            db_path = os.path.join(base_dir, "mediciones.db")

        self.con = sqlite3.connect(db_path, check_same_thread=False)
        self.cursor = self.con.cursor()

    def insertar_medicion(self, id_sensor,valor_contador):
        """insertar_medicion
        Inserta una medicion en la tabla mediciones.
        Args:
            id_sensor (int): numero id del sensor.
            valor_contador(int): contador del del sensor.
        """
        self.cursor.execute("INSERT INTO mediciones (ID_SENSOR,VALOR_CONTADOR) VALUES (?, ?)", (id_sensor,valor_contador))
        self.con.commit()
    
    
    def get_ultima_medicion(self):
        """get_ultima_medicion
        Obtiene la ultima medicion de la tabla mediciones.

        Devuelve:
            tuple: tupla con la ultima medicion (ID, ID_SENSOR,VALOR_CONTADOR)
        """
        self.cursor.execute("SELECT * FROM mediciones ORDER BY id DESC LIMIT 1")
        return self.cursor.fetchone()

    
    def get_ultimas_x_mediciones(self,cuantas):
        """get_ultimas_x_mediciones
        Obtiene las ultimas x mediciones de la tabla mediciones.

        Args:
            cuantas (int): numero de mediciones a obtener.

        Devuelve:
            list: lista de tuplas con las ultimas x mediciones (ID, ID_SENSOR,VALOR_CONTADOR)
        """
        cuantas=str(cuantas)
        self.cursor.execute(f"SELECT * FROM mediciones ORDER BY id DESC LIMIT {cuantas}")
        return self.cursor.fetchall()
        
        
    def cerrar_conexion(self):
        """cerrar_conexion
        Cierra la conexion a la base de datos.
        """
        # Cierra la conexion a la base de datos.
        self.con.close()
        
    