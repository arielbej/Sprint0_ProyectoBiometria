package com.example.aabejgom.bltea_alumnos;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/********************************************
 * @file LogicaFake.java
 * @brief clase que actua como logica fake del telefono. Inserta y obtiene datos de la bbd usando
 * GET y POST
 *******************************************/

public class LogicaFake {
    private final String baseUrl;
    public LogicaFake(String baseUrl) {
        this.baseUrl = baseUrl; // ej: "http://127.0.0.1:5000"
    }

    public void insertarMedicion(int id_gas, int valor) throws Exception {

        URL url = new URL(baseUrl + "/mediciones");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        // JSON que mandamos a la API
        String jsonInput = "{ \"id_gas\": " + id_gas + ", \"valor\": " + valor + " }";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Leer respuesta
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Log.d("LOGICAFAKE", response.toString());
        }

    }
}


