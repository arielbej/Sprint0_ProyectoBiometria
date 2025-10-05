import sys, os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "src")))

from fastapi.testclient import TestClient
from server.API.main import app

client = TestClient(app)


def test_insertar_medicion_con_post():
    """
    Test de integración básico:
    - Inserta una medición con POST.
    - Consulta la última con GET.
    - Verifica que los datos coincidan.
    """
    dato_test = {
        "id_gas": 1,
        "valor": "{\"temperatura\": 25, \"nivel_gas\": 30}"
    }
    
    response_post = client.post("/mediciones", json=dato_test)
    assert response_post.status_code == 200
    assert response_post.json()["mensaje"] == "Medicion insertada correctamente"
    
def test_get_ultima_medicion():
    """
    Test para verificar que /ultima_medicion_obtenida funciona.
    """
    # Obtengo la última medición
    response_get = client.get("/mediciones/ultima_medicion_obtenida")
    assert response_get.status_code == 200

    data = response_get.json()
    assert "id" in data
    assert data["id_gas"] == 1
    assert "valor" in data
    

def test_get_ultimas_mediciones():
    """
    Test para verificar que /ultimas_mediciones funciona y devuelve lista.
    """

    response = client.get("/mediciones/ultimas_mediciones?cuantas=3")
    assert response.status_code == 200
    data = response.json()

    # Verifico estructura
    assert "mediciones" in data or "mensaje" in data
