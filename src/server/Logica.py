
import sqlite3

class Logica:
    """Logica
    Clase que define la logica de negocio de la aplicacion.
    Nota: la conexion a la base de datos se abre al crear la instancia de la clase.
    
    Args_constructor: db_path: ruta a la base de datos sqlite (por defecto "src/server/bbd/mediciones.db")
    
    """
    def __init__(self,db_path="src/server/bbd/mediciones.db"):
        self.con = sqlite3.connect(db_path)
        self.cursor = self.con.cursor()

    def insertar_medicion(self, id_gas,valor):
        """insertar_medicion
        Inserta una medicion en la tabla mediciones.
        Args:
            id_gas (int): numero id del gas.
            valor (string): el valor de la medidada en formato json({"temperatura":35,"nivel_gas":30%} por ejemplo).
        """
        self.cursor.execute("INSERT INTO mediciones (ID_GAS,VALOR) VALUES (?, ?)", (id_gas,valor))
        self.con.commit()
    
    
    def get_ultima_medicion(self):
        """get_ultima_medicion
        Obtiene la ultima medicion de la tabla mediciones.

        Devuelve:
            tuple: tupla con la ultima medicion (ID, ID_GAS, VALOR)
        """
        self.cursor.execute("SELECT * FROM mediciones ORDER BY id DESC LIMIT 1")
        return self.cursor.fetchone()

    
    def get_ultimas_x_mediciones(self,cuantas):
        """get_ultimas_x_mediciones
        Obtiene las ultimas x mediciones de la tabla mediciones.

        Args:
            cuantas (int): numero de mediciones a obtener.

        Devuelve:
            list: lista de tuplas con las ultimas x mediciones (ID, ID_GAS, VALOR)
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
        
    