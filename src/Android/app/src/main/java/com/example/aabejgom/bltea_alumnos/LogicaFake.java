package com.example.aabejgom.bltea_alumnos;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/********************************************
 * @file LogicaFake.java
 * @brief clase que actua como logica fake del telefono. Inserta y obtiene datos de la bbd usando
 * GET y POST
 *******************************************/

public class LogicaFake {
    private String baseUrl;
    public LogicaFake() {
        this.baseUrl = baseUrl; // ej: "http://127.0.0.1:5000"
    }

    public void insertarMedicion(int id, int medicion)throws Exception {
        URL url = new URL(baseUrl + "/insertar");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        // JSON que mandamos a la API
        String jsonInput = "{ \"id\": " + id + ", \"medicion\": " + medicion + " }";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Leer respuesta
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Log.d("LOGICAFAKE", response.toString());
        }

    }

    // Simula GET -> pedir la medición más reciente
    public String getMedicion(int id) throws Exception {
        URL url = new URL(baseUrl + "/get/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    // Simula GET -> obtener la última medición
    public String getUltimaMedicion() throws Exception {
        URL url = new URL(baseUrl + "/ultima");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

}

