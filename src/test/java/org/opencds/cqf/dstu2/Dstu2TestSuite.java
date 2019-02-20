package org.opencds.cqf.dstu2;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.opencds.cqf.TestHelper;
import org.opencds.cqf.client.ConnectionFactory;
import org.opencds.cqf.client.RequestFactory;
import org.opencds.cqf.client.RequestHelper;
import org.opencds.cqf.dstu3.Dstu3CRUDTests;

public class Dstu2TestSuite {

    private String baseDstu2Url;
    private String cdsDstu2Url;

    private RequestHelper helper;

    @Before
    public void setup() {
        baseDstu2Url = System.getProperty("base.dstu2");
        cdsDstu2Url = System.getProperty("cds.dstu2");
        helper = new RequestHelper();
    }

    @Test
    @Category(Dstu3CRUDTests.class)
    public void metadata_test() {
        String response = RequestFactory.makeGetRequest(
                ConnectionFactory.createConnection(baseDstu2Url + "/metadata")
        );

        try {
            JsonElement conformance = helper.getJsonProperty(response, "resourceType");
            Assert.assertTrue(conformance.getAsString().equals("Conformance"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    @Category(Dstu2CRUDTests.class)
    public void create_test() {
        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu2Url + "/Patient",
                TestHelper.getResource("patient_create.json", this.getClass())
        );

        JsonElement id = helper.getJsonProperty(response, "id");
        Assert.assertTrue(id != null);

        TestHelper.delete(baseDstu2Url + "/Patient/" + id.getAsString());
    }

    @Test
    @Category(Dstu2CRUDTests.class)
    public void update_test() {
        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/Patient-12214",
                TestHelper.getResource("patient_update.json", this.getClass())
        );

        JsonElement id = helper.getJsonProperty(response, "id");
        Assert.assertTrue(id != null && id.getAsString().equals("Patient-12214"));

        TestHelper.delete(baseDstu2Url + "/Patient/Patient-12214");
    }
}
