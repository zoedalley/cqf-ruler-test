package org.opencds.cqf;

import org.junit.Assert;
import org.opencds.cqf.client.ConnectionFactory;
import org.opencds.cqf.client.RequestFactory;

import java.io.InputStream;
import java.util.Scanner;

public class TestHelper {

    public static String getResource(String fileName, Class clazz) {
        InputStream is = clazz.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public static void delete(String url) {
        try {
            RequestFactory.makeDeleteRequest(
                    ConnectionFactory.createConnection(url)
            );
            // Check delete was successful
            String response = RequestFactory.makeGetRequest(
                    ConnectionFactory.createConnection(url)
            );
            Assert.assertTrue(response.equals("410"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public static void batchDelete(String ... urls) {
        for (String url : urls) {
            delete(url);
        }
    }
}
