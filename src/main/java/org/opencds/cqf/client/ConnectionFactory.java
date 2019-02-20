package org.opencds.cqf.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionFactory {

    public static HttpURLConnection createConnection(String url) {
        try {
            return (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Could not connect to: " + url + "\nError: " + e.getMessage()
            );
        }
    }
}
