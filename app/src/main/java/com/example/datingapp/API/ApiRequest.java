package com.example.datingapp.API;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ApiRequest { // Arquivo deprecated
    private String method;
    private String link;
    private HashMap<String, String> headers;
    private JSONObject bodyJson;

    // Construtor
    public ApiRequest(String method, String link, HashMap<String, String> headers, JSONObject bodyJson) {
        this.method = method.toUpperCase();
        this.link = link;
        this.headers = headers != null ? headers : new HashMap<>();
        this.bodyJson = bodyJson;
    }

    // Método para executar a requisição
    public String execute() throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Configura cabeçalhos
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            if (!headers.containsKey("Content-Type")) {
                connection.setRequestProperty("Content-Type", "application/json");
            }
            if (!headers.containsKey("Accept")) {
                connection.setRequestProperty("Accept", "application/json");
            }

            // Adiciona corpo da requisição para métodos POST e PUT
            if (bodyJson != null && (method.equals("POST") || method.equals("PUT"))) {
                connection.setDoOutput(true);
                String jsonBody = bodyJson.toString();
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(jsonBody.getBytes("UTF-8"));
                    os.flush();
                }
            }

            // Lê a resposta da API
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300
                            ? connection.getInputStream()
                            : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            String responseString = response.toString();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + responseString);

            try {
                JSONObject jsonResponse = new JSONObject(responseString);
                return jsonResponse.toString(); // JSON válido
            } catch (Exception e) {
                System.out.println("Resposta não é um JSON válido: " + responseString);
                throw new Exception("Erro ao processar a resposta: " + responseString);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


}