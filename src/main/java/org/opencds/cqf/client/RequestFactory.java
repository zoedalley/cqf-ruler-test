package org.opencds.cqf.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;

public class RequestFactory {

    public enum RequestType {
        GET,
        POST,
        PUT,
        DELETE
    }

    private static String getResponse(HttpURLConnection conn) {
        StringBuilder response = new StringBuilder();
        try {
            int status = conn.getResponseCode();
            if (status == 410) {
                return "410";
            }
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error getting response status code: " + ioe.getMessage()
            );
        }
        try (
                Reader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream(),
                                "UTF-8"
                        )
                )
        ) {
            for (int i; (i = in.read()) >= 0; ) {
                response.append((char) i);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error getting response: " + ioe.getMessage()
            );
        }
        conn.disconnect();
        return response.toString();
    }

    public static String makePostRequest(HttpURLConnection conn, String body) {
        byte[] data = body.getBytes();
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.getOutputStream().write(data);

            return getResponse(conn);
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error making POST request: " + ioe.getMessage()
            );
        }
    }

    public static String makePutRequest(HttpURLConnection conn, String body) {
        byte[] data = body.getBytes();
        try {
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.getOutputStream().write(data);

            return getResponse(conn);
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error making PUT request: " + ioe.getMessage()
            );
        }
    }

    public static String makeGetRequest(HttpURLConnection conn) {
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            return getResponse(conn);
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error making GET request: " + ioe.getMessage()
            );
        }
    }

    public static String makeDeleteRequest(HttpURLConnection conn) {
        try {
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");
            return getResponse(conn);
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error making DELETE request: " + ioe.getMessage()
            );
        }
    }

    public static String makeRequest(RequestType type, String url, String data) {
        switch (type) {
            case GET: return makeGetRequest(ConnectionFactory.createConnection(url));
            case POST: return makePostRequest(ConnectionFactory.createConnection(url), data);
            case PUT: return makePutRequest(ConnectionFactory.createConnection(url), data);
            case DELETE: return makeDeleteRequest(ConnectionFactory.createConnection(url));
            default: throw new RuntimeException("Unknown request type");
        }
    }
}
