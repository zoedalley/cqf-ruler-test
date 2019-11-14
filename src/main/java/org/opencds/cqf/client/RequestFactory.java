package org.opencds.cqf.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

// TODO: Log each request and the status of the response
// TODO: Enable data purge (DELETE request is not sufficient for HAPI FHIR server)

public class RequestFactory {

    public enum RequestType {
        GET,
        POST,
        PUT
    }

    private static String makePostRequest(String url, String body) {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        String result;
        try {
            post.setEntity(new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    "Invalid encoding: JSON encoding expected\nError: " + e.getMessage()
            );
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post))
        {
            result = EntityUtils.toString(response.getEntity());
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "Error making POST request: " + e.getMessage()
            );
        }

        return result;
    }

    private static String makePutRequest(String url, String body) {
        HttpPut put = new HttpPut(url);
        put.addHeader("Content-Type", "application/json");
        String result;
        try {
            put.setEntity(new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    "Invalid encoding: JSON encoding expected\nError: " + e.getMessage()
            );
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(put))
        {
            result = EntityUtils.toString(response.getEntity());
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "Error making PUT request: " + e.getMessage()
            );
        }

        return result;
    }

    private static String makeGetRequest(String url) {
        HttpGet get = new HttpGet(url);
        String result;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get))
        {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Error encountered during GET request: " + EntityUtils.toString(response.getEntity()));
            }
            result = EntityUtils.toString(response.getEntity());
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "Error making GET request: " + e.getMessage()
            );
        }

        return result;
    }

    public static String makeRequest(RequestType type, String url, String data) {
        switch (type) {
            case GET: return makeGetRequest(url);
            case POST: return makePostRequest(url, data);
            case PUT: return makePutRequest(url, data);
            default: throw new RuntimeException("Unknown request type");
        }
    }
}
