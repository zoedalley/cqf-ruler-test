package org.opencds.cqf.gen;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Dstu2OpioidRequestGenerator {

    private static final String RELATIVE_PATH = "src/main/resources/";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static JsonObject createBundle(List<JsonObject> contextResources) {
        JsonObject bundle = new JsonObject();
        bundle.add("resourceType", new JsonPrimitive("Bundle"));
        JsonArray entryResources = new JsonArray();
        for (JsonObject resource : contextResources) {
            JsonObject entryResource = new JsonObject();
            entryResource.add("resource", resource);
            entryResources.add(entryResource);
        }
        bundle.add("entry", entryResources);
        return bundle;
    }

    public static String createRequest(String server, String hook, String patientId,
                                       String encounterId, String contextResourceFieldName,
                                       List<JsonObject> contextResources,
                                       Map<String, JsonObject> prefetchResources)
    {
        JsonObject request = new JsonObject();
        request.add("hookInstance", new JsonPrimitive(UUID.randomUUID().toString()));
        request.add("fhirServer", new JsonPrimitive(server));
        request.add("hook", new JsonPrimitive(hook));
        request.add("user", new JsonPrimitive("Practitioner/example"));

        JsonObject context = new JsonObject();
        context.add("patientId", new JsonPrimitive(patientId));
        context.add("encounterId", new JsonPrimitive(encounterId));
        if (contextResourceFieldName != null && contextResources != null) {
            context.add(contextResourceFieldName, createBundle(contextResources));
        }

        request.add("context", context);

        JsonObject prefetch = new JsonObject();
        for (Map.Entry<String, JsonObject> entry : prefetchResources.entrySet()) {
            JsonObject item = new JsonObject();
            JsonObject response = new JsonObject();
            response.add("status", new JsonPrimitive("200 OK"));
            item.add("response", response);
            item.add("resource", entry.getValue() == null ? JsonNull.INSTANCE : entry.getValue());
            prefetch.add(entry.getKey(), item);
        }

        request.add("prefetch", prefetch);

        String retVal = gson.toJson(request);
        outputRequest(patientId.replaceFirst("patient-", "request-"), retVal);
        return retVal;
    }

    public static String createMedicationPrescribeRequest(String server, String patientId,
                                       String encounterId, List<JsonObject> contextResources,
                                       Map<String, JsonObject> prefetchResources)
    {
        return createRequest(
                server, "medication-prescribe", patientId,
                encounterId, "medications", contextResources,
                prefetchResources
        );
    }

    public static String createPatientViewRequest(String server, String patientId,
                                                  String encounterId, Map<String, JsonObject> prefetchResources)
    {
        return createRequest(
                server, "patient-view", patientId,
                encounterId, null, null,
                prefetchResources
        );
    }

    private static void outputRequest(String id, String request) {
        try (PrintWriter pw = new PrintWriter(
                Paths.get(RELATIVE_PATH + id + ".json").toFile(),
                "UTF-8"))
        {
            pw.println(request);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.err.println("Unable to output request: " + id);
        }
    }
}
