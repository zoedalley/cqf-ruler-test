package org.opencds.cqf.gen;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class Dstu2OpioidTestDataGenerator {

    private final String RELATIVE_PATH = "src/main/resources/";

    private Gson gson;
    private JsonArray testResources;

    public Dstu2OpioidTestDataGenerator() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        testResources = new JsonArray();
    }

    public String getAsString(JsonObject obj) {
        return gson.toJson(obj);
    }

    public JsonObject createReference(String resourcePart, String idPart) {
        JsonObject reference = new JsonObject();
        reference.add("reference", new JsonPrimitive(String.format("%s/%s", resourcePart, idPart)));
        return reference;
    }

    public JsonObject createPeriod(String start, String end) {
        JsonObject period = new JsonObject();
        period.add("start", new JsonPrimitive(start));
        period.add("end", new JsonPrimitive(end));
        return period;
    }

    public JsonObject createCodeableConcept(String system, String code, String display) {
        JsonObject concept = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject codeObj = new JsonObject();
        codeObj.add("system", new JsonPrimitive(system));
        codeObj.add("code", new JsonPrimitive(code));
        if (display != null) {
            codeObj.add("display", new JsonPrimitive(display));
        }
        coding.add(codeObj);
        concept.add("coding", coding);
        return concept;
    }

    public JsonObject createQuantity(double value, String unit) {
        JsonObject quantity = new JsonObject();
        quantity.add("value", new JsonPrimitive(value));
        quantity.add("unit", new JsonPrimitive(unit));
        return quantity;
    }

    public JsonObject createDefaultPatient(String id, String gender) {
        JsonObject patient = new JsonObject();
        patient.add("resourceType", new JsonPrimitive("Patient"));
        patient.add("id", new JsonPrimitive(id));
        patient.add("gender", new JsonPrimitive(gender));
        patient.add("birthDate", new JsonPrimitive(LocalDate.of(1982, 1, 7).toString()));

        addTestResource(patient, "Patient", id);

        return patient;
    }

    public String createDefaultPatientAsString(String id, String gender) {
        return gson.toJson(createDefaultPatient(id, gender));
    }

    public JsonObject createEncounter(String id, String patientId, String start, String end) {
        JsonObject encounter = new JsonObject();
        encounter.add("resourceType", new JsonPrimitive("Encounter"));
        encounter.add("id", new JsonPrimitive(id));
        encounter.add("status", new JsonPrimitive("finished"));
        encounter.add("patient", createReference("Patient", patientId));
        encounter.add("period", createPeriod(start, end));

        addTestResource(encounter, "Encounter", id);

        return encounter;
    }

    public JsonObject createEncounterToday(String id, String patientId) {
        return createEncounter(id, patientId, LocalDate.now().toString(), LocalDate.now().toString());
    }

    public String createEncounterTodayAsString(String id, String patientId) {
        return gson.toJson(createEncounterToday(id, patientId));
    }

    public JsonObject createMedicationOrder(String id, String dateWritten, String patientId,
                                            String encounterId, String code, String display,
                                            int frequency, double period, String periodUnits,
                                            double doseValue, String doseUnit, String start,
                                            String end, int repeats, double supplyValue,
                                            String supplyUnit)
    {
        JsonObject order = new JsonObject();
        order.add("resourceType", new JsonPrimitive("MedicationOrder"));
        order.add("id", new JsonPrimitive(id));
        order.add("dateWritten", new JsonPrimitive(dateWritten));
        order.add("status", new JsonPrimitive("active"));
        order.add("patient", createReference("Patient", patientId));
        order.add("encounter", createReference("Encounter", encounterId));
        order.add("medicationCodeableConcept", createCodeableConcept("http://www.nlm.nih.gov/research/umls/rxnorm", code, display));

        JsonArray dosageInstruction = new JsonArray();
        JsonObject dosage = new JsonObject();
        JsonObject timing = new JsonObject();
        JsonObject repeat = new JsonObject();
        repeat.add("frequency", new JsonPrimitive(frequency));
        repeat.add("period", new JsonPrimitive(period));
        repeat.add("periodUnits", new JsonPrimitive(periodUnits));
        timing.add("repeat", repeat);
        dosage.add("timing", timing);
        dosage.add("asNeededBoolean", new JsonPrimitive(false));
        dosage.add("doseQuantity", createQuantity(doseValue, doseUnit));
        dosageInstruction.add(dosage);

        order.add("dosageInstruction", dosageInstruction);

        JsonObject dispenseRequest = new JsonObject();
        dispenseRequest.add("validityPeriod", createPeriod(start, end));
        dispenseRequest.add("numberOfRepeatsAllowed", new JsonPrimitive(repeats));
        dispenseRequest.add("expectedSupplyDuration", createQuantity(supplyValue, supplyUnit));

        order.add("dispenseRequest", dispenseRequest);

        addTestResource(order, "MedicationOrder", id);

        return order;
    }

    public JsonObject createContextMedicationOrder(String id, String patientId, String encounterId,
                                                   String code, String display, int frequency, double period,
                                                   String periodUnits, double doseValue, String doseUnit,
                                                   int repeats, double supplyValue, String supplyUnit)
    {
        return createMedicationOrder(
                id, LocalDate.now().toString(), patientId, encounterId, code, display,
                frequency, period, periodUnits, doseValue, doseUnit, LocalDate.now().toString(),
                LocalDate.now().plusMonths(3).toString(), repeats, supplyValue, supplyUnit
        );
    }

    public JsonObject createObservation(String id, String code, String display,
                                        String patientId, String effective,
                                        String interpretationCode)
    {
        JsonObject observation = new JsonObject();
        observation.add("resourceType", new JsonPrimitive("Observation"));
        observation.add("id", new JsonPrimitive(id));
        observation.add("status", new JsonPrimitive("final"));
        observation.add("code", createCodeableConcept("http://loinc.org", code, display));
        observation.add("subject", createReference("Patient", patientId));
//        observation.add("encounter", createReference("Encounter", encounterId));
        observation.add("effectiveDateTime", new JsonPrimitive(effective));
        observation.add("interpretation", createCodeableConcept("http://hl7.org/fhir/v2/0078", interpretationCode, null));

        addTestResource(observation, "Observation", id);

        return observation;
    }

    public String createObservationAsString(String id, String code, String display,
                                        String patientId, String encounterId,
                                        String effective, String interpretationCode)
    {
        return gson.toJson(
                createObservation(
                        id, code, display, patientId, effective, interpretationCode
                )
        );
    }

    public JsonObject createBundle(String type, JsonArray entryResources) {
        JsonObject bundle = new JsonObject();
        bundle.add("resourceType", new JsonPrimitive("Bundle"));
        if (type != null) {
            bundle.add("type", new JsonPrimitive(type));
        }
        bundle.add("entry", entryResources);
        return bundle;
    }

    public JsonObject createBundle(String type, List<JsonObject> entryResourceList) {
        JsonArray entryResourceArr = new JsonArray();
        for (JsonObject resource : entryResourceList) {
            JsonObject entryResource = new JsonObject();
            entryResource.add("resource", resource);
            entryResourceArr.add(entryResource);
        }
        // todo: check bundle type for additional requirements like request for transaction

        return createBundle(type, entryResourceArr);
    }

    public void outputTestBundle() {
        JsonObject bundle = createBundle("transaction", testResources);
        outputBundle("dstu2-opioid-test-bundle.json", bundle);
    }

    public void outputBundle(String fileName, JsonObject bundle) {
        try (PrintWriter pw = new PrintWriter(
                Paths.get(RELATIVE_PATH + fileName).toFile(),
                "UTF-8"))
        {
            pw.println(gson.toJson(bundle));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.err.println("Unable to output test data");
        }
    }

    public void outputBasicRecBundle(JsonObject patient, JsonObject encounter, String fileName)
    {
        JsonArray entry = new JsonArray();

        JsonObject patientEntry = new JsonObject();
        patientEntry.add("resource", patient);

        JsonObject patientRequest = new JsonObject();
        patientRequest.add("method", new JsonPrimitive("PUT"));
        patientRequest.add("url", new JsonPrimitive("Patient/" + patient.get("id").getAsString()));

        patientEntry.add("request", patientRequest);
        entry.add(patientEntry);

        JsonObject encounterEntry = new JsonObject();
        encounterEntry.add("resource", encounter);

        JsonObject encounterRequest = new JsonObject();
        encounterRequest.add("method", new JsonPrimitive("PUT"));
        encounterRequest.add("url", new JsonPrimitive("Encounter/" + encounter.get("id").getAsString()));

        encounterEntry.add("request", encounterRequest);
        entry.add(encounterEntry);

        outputBundle(fileName, createBundle("transaction", entry));
    }
    
    private void addTestResource(JsonObject resource, String resourceType, String id) {
        JsonObject testResource = new JsonObject();
        testResource.add("resource", resource);

        JsonObject request = new JsonObject();
        request.add("method", new JsonPrimitive("PUT"));
        request.add("url", new JsonPrimitive(resourceType + "/" + id));

        testResource.add("request", request);
        testResources.add(testResource);
    }
}
