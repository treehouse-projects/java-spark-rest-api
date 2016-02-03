package com.teamtreehouse.testing;

import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    private String server;

    public ApiClient(String server) {
        this.server = server;
    }

    public ApiResponse request(String method, String uri) {
        return request(method, uri, null);
    }

    public ApiResponse request(String method, String uri, String requestBody) {
        try {
            URL url = new URL(server + uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            if (requestBody != null) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.getBytes("UTF-8"));
                }
            }
            connection.connect();
            InputStream inputStream = connection.getResponseCode() < 400 ?
                    connection.getInputStream() :
                    connection.getErrorStream();
            String body = IOUtils.toString(inputStream);
            return new ApiResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Whoops!  Connection error");
        }
    }
}