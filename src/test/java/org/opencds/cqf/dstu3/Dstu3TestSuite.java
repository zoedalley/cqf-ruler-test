package org.opencds.cqf.dstu3;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.opencds.cqf.TestHelper;
import org.opencds.cqf.client.RequestFactory;
import org.opencds.cqf.client.RequestHelper;

public class Dstu3TestSuite {

    private String baseDstu3Url;

    private RequestHelper helper;

    @Before
    public void setup() {
        baseDstu3Url = System.getProperty("base.dstu3");
        helper = new RequestHelper();
    }

    @Test
    @Category(Dstu3CRUDTests.class)
    public void metadata_test() {
        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.GET, baseDstu3Url + "/metadata", null
        );

        try {
            JsonElement conformance = helper.getJsonProperty(response, "resourceType");
            Assert.assertTrue(conformance.getAsString().equals("CapabilityStatement"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    @Category(Dstu3CRUDTests.class)
    public void create_test() {
        // simple test that creates a Patient via POST and then deletes it
        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url + "/Patient",
                TestHelper.getResource("patient_create.json", this.getClass())
        );

        JsonElement id = helper.getJsonProperty(response, "id");
        Assert.assertTrue(id != null);

        TestHelper.delete(baseDstu3Url + "/Patient/" + id.getAsString());
    }

    @Test
    @Category(Dstu3CRUDTests.class)
    public void update_test() {
        // simple test that creates/updates a Patient via PUT and then deletes it
        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/Patient-12214",
                TestHelper.getResource("patient_update.json", this.getClass())
        );

        JsonElement id = helper.getJsonProperty(response, "id");
        Assert.assertTrue(id != null && id.getAsString().equals("Patient-12214"));

        TestHelper.delete(baseDstu3Url + "/Patient/Patient-12214");
    }

    @Test
    @Category(Dstu3CRUDTests.class)
    public void transaction_test() {
        // simple test that creates resources via transaction and then deletes them via transaction
        // Must create and delete patient outside the transaction because there is an issue when running this multiple times:
        //      "InvalidRequestException: Resource Patient/Patient-22276 is deleted, specified in path: Observation.subject"
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/Patient-22276",
                TestHelper.getResource("patient_update_2.json", this.getClass())
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource("transaction_put_bundle.json", this.getClass())
        );

        JsonElement type = helper.getJsonProperty(response, "type");
        Assert.assertTrue(type != null && type.getAsString().equals("transaction-response"));

        response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource("transaction_delete_bundle.json", this.getClass())
        );

        type = helper.getJsonProperty(response, "type");
        Assert.assertTrue(type != null && type.getAsString().equals("transaction-response"));

        // deleting patient
        TestHelper.delete(baseDstu3Url + "/Patient/Patient-22276");
    }
}
