package org.opencds.cqf.dstu2.cds.opioid;

import static org.hamcrest.Matchers.equalTo;

import com.google.gson.JsonObject;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.rules.ErrorCollector;
import org.opencds.cqf.TestHelper;
import org.opencds.cqf.client.RequestFactory;
import org.opencds.cqf.dstu3.cds.opioid.Dstu3OpioidCdsTestSuite;
import org.opencds.cqf.gen.Dstu2OpioidRequestGenerator;
import org.opencds.cqf.gen.Dstu2OpioidTestDataGenerator;

import java.time.LocalDate;
import java.util.*;

public class Dstu2OpioidCdsMedicationPrescribeTestSuite {

    private static final String TERMINOLOGY_BUNDLE_FILE = "opioid_cds_terminology_bundle.json";
    private static final String FHIRHELPERS_LIBRARY_FILE = "library_FHIRHelpers.json";
    private static final String OMTKDATA_LIBRARY_FILE = "library_OMTKData.json";
    private static final String OMTKLOGIC_LIBRARY_FILE = "library_OMTKLogic.json";
    private static final String COMMON_LIBRARY_FILE = "library_OpioidCDSCommonDSTU2.json";

    private static final String REC_10_LIBRARY_FILE = "library_opioidcds_recommendation_10_dstu2.json";
    private static final String REC_10_PATIENT_VIEW_LIBRARY_FILE = "library_opioidcds_recommendation_10_dstu2_patient_view.json";
    private static final String REC_10_PLANDEF_FILE = "plandefinition_opioidcds_10_dstu2.json";
    private static final String REC_10_PATIENT_VIEW_PLANDEF_FILE = "plandefinition_opioidcds_10_dstu2_patient_view.json";
    private static final String REC_11_LIBRARY_FILE = "library_opioidcds_recommendation_11_dstu2.json";
    private static final String REC_11_PLANDEF_FILE = "plandefinition_opioidcds_11_dstu2.json";

    private static String baseDstu3Url;
    private static String baseDstu2Url;
    private static String cdsDstu2Url;

    private static Dstu2OpioidTestDataGenerator generator;

    @BeforeClass
    public static void setup() {
        baseDstu3Url = System.getProperty("base.dstu3");
        baseDstu2Url = System.getProperty("base.dstu2");
        cdsDstu2Url = System.getProperty("cds.dstu2");
        generator = new Dstu2OpioidTestDataGenerator();

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(TERMINOLOGY_BUNDLE_FILE, Dstu3OpioidCdsTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/FHIRHelpers",
                TestHelper.getResource(FHIRHELPERS_LIBRARY_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/OMTKData",
                TestHelper.getResource(OMTKDATA_LIBRARY_FILE, Dstu3OpioidCdsTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/OMTKLogic",
                TestHelper.getResource(OMTKLOGIC_LIBRARY_FILE, Dstu3OpioidCdsTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/OpioidCDSCommonDSTU2",
                TestHelper.getResource(COMMON_LIBRARY_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );

        // Recommendation 10 artifacts
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/opioidcds-recommendation-10-dstu2",
                TestHelper.getResource(REC_10_LIBRARY_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/PlanDefinition/opioidcds-10-dstu2",
                TestHelper.getResource(REC_10_PLANDEF_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );

        // Recommendation 10 patient-view artifacts
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/opioidcds-recommendation-10-dstu2-patient-view",
                TestHelper.getResource(REC_10_PATIENT_VIEW_LIBRARY_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/PlanDefinition/opioidcds-10-dstu2-patient-view",
                TestHelper.getResource(REC_10_PATIENT_VIEW_PLANDEF_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );

        // Recommendation 11 artifacts
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/opioidcds-recommendation-11-dstu2",
                TestHelper.getResource(REC_11_LIBRARY_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/PlanDefinition/opioidcds-11-dstu2",
                TestHelper.getResource(REC_11_PLANDEF_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );
    }

    //@AfterClass
    public static void destroy() {
        TestHelper.delete(baseDstu3Url + "/PlanDefinition/opioidcds-10-dstu2");
        TestHelper.delete(baseDstu3Url + "/Library/opioidcds-recommendation-10-dstu2");
        TestHelper.delete(baseDstu3Url + "/PlanDefinition/opioidcds-10-dstu2-patient-view");
        TestHelper.delete(baseDstu3Url + "/Library/opioidcds-recommendation-10-dstu2-patient-view");
        TestHelper.delete(baseDstu3Url + "/PlanDefinition/opioidcds-11-dstu2");
        TestHelper.delete(baseDstu3Url + "/Library/opioidcds-recommendation-11-dstu2");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource("opioid_cds_cleanup_bundle.json", Dstu3OpioidCdsTestSuite.class)
        );

        TestHelper.delete(baseDstu3Url + "/Library/OpioidCDSCommonDSTU2");
        TestHelper.delete(baseDstu3Url + "/Library/OMTKLogic");
        TestHelper.delete(baseDstu3Url + "/Library/OMTKData");
        TestHelper.delete(baseDstu3Url + "/Library/FHIRHelpers");

        generator.outputTestBundle();
    }

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_end_of_life_med_exclusion_test() {
        String patientId = "patient-rec-10-end-of-life";
        String encounterId = "encounter-context-rec-10-end-of-life";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-10-end-of-life-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-10-end-of-life", patientId, encounterId,
                        "1012727", "Carbinoxamine maleate 0.4 MG/ML / Hydrocodone Bitartrate 1 MG/ML / Pseudoephedrine Hydrochloride 6 MG/ML Oral Solution",
                        1, 12.0, "d", 5.0, "mL",
                        3, 30.0, "d"
                )
        );
        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2",
                request
        );
        String expected = "{\n" + "  \"cards\": []\n" + "}\n";
        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_illicit_drugs_test() {
        String patientId = "patient-rec-10-illicit-drugs";
        String encounterId = "encounter-context-rec-10-illicit-drugs";
        String prefetchEncounterId = "encounter-prefetch-rec-10-illicit-drugs";
        String observationId = "observation-rec-10-illicit-drugs";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-10-illicit-drugs-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-10-illicit-drugs", patientId, encounterId,
                        "197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                        1, 12.0, "d", 1.0, "patch",
                        3, 30.0, "d"
                )
        );
        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-prefetch-rec-10-illicit-drugs", LocalDate.now().minusDays(90).toString(),
                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        3, 1.0, "d", 1.0, "tablet",
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().toString(),
                        3, 30.0, "d"
                )
        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        prefetchEncounterId, patientId,
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().minusDays(90).toString()
                )
        );
        prefetchResources.put(
                "item4",
                generator.createObservation(
                        observationId, "3426-4", "Tetrahydrocannabinol [Presence] in Urine",
                        patientId, LocalDate.now().minusDays(28).toString(), "POS"
                )
        );
        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2",
                request
        );
        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Illicit Drugs Found In Urine Screening\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"Found the following illicit drug(s) in urine drug screen: Tetrahydrocannabinol\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://guidelines.gov/summaries/summary/50153/cdc-guideline-for-prescribing-opioids-for-chronic-pain---united-states-2016#420\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_missing_prescribed_opioids_test() {
        String patientId = "patient-rec-10-missing-prescribed-opioids";
        String encounterId = "encounter-context-rec-10-missing-prescribed-opioids";
        String prefetchEncounterId = "encounter-prefetch-rec-10-missing-prescribed-opioids";
        String observationId = "observation-rec-10-missing-prescribed-opioids";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-10-missing-prescribed-opioids-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-10-missing-prescribed-opioids", patientId, encounterId,
                        "197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                        1, 12.0, "d", 1.0, "patch",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        prefetchResources.put(
                "item2",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createMedicationOrder(
                                        "order-existing-rec-10-missing-prescribed-opioids", LocalDate.now().minusDays(90).toString(),
                                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                                        3, 1.0, "d", 1.0, "tablet",
                                        LocalDate.now().minusDays(90).toString(), LocalDate.now().toString(),
                                        3, 30.0, "d"
                                ),
                                generator.createMedicationOrder(
                                        "order-missing-rec-10-missing-prescribed-opioids", LocalDate.now().minusDays(90).toString(),
                                        patientId, prefetchEncounterId, "197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                                        3, 1.0, "d", 1.0, "patch",
                                        LocalDate.now().minusDays(90).toString(), LocalDate.now().toString(),
                                        3, 30.0, "d"
                                )
                        )
                )

        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        prefetchEncounterId, patientId,
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().minusDays(90).toString()
                )
        );
        prefetchResources.put(
                "item4",
                generator.createObservation(
                        observationId, "10998-3", "Oxycodone [Presence] in Urine",
                        patientId, LocalDate.now().minusDays(28).toString(), "POS"
                )
        );

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Prescribed Opioids Not Found In Urine Screening\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"The following opioids are missing from the screening: fentanyl\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://guidelines.gov/summaries/summary/50153/cdc-guideline-for-prescribing-opioids-for-chronic-pain---united-states-2016#420\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_no_screenings_test() {
        String patientId = "patient-rec-10-no-screenings";
        String encounterId = "encounter-context-rec-10-no-screenings";
        String prefetchEncounterId = "encounter-prefetch-rec-10-no-screenings";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-10-no-screenings-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-10-no-screenings", patientId, encounterId,
                        "197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                        1, 12.0, "d", 1.0, "patch",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-prefetch-rec-10-no-screenings", LocalDate.now().minusDays(90).toString(),
                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        3, 1.0, "d", 1.0, "tablet",
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().toString(),
                        3, 30.0, "d"
                )
        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        prefetchEncounterId, patientId,
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().minusDays(90).toString()
                )
        );

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Annual Urine Screening Check\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"Patients on opioid therapy should have a urine drug test performed every 12 months.\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://guidelines.gov/summaries/summary/50153/cdc-guideline-for-prescribing-opioids-for-chronic-pain---united-states-2016#420\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_not_missing_prescribed_opioids_test() {
        String patientId = "patient-rec-10-not-missing-prescribed-opioids";
        String encounterId = "encounter-context-rec-10-not-missing-prescribed-opioids";
        String prefetchEncounterId = "encounter-prefetch-rec-10-not-missing-prescribed-opioids";
        String observationId = "observation-rec-10-not-missing-prescribed-opioids";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-10-not-missing-prescribed-opioids-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-10-not-missing-prescribed-opioids", patientId, encounterId,
                        "197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                        1, 12.0, "d", 1.0, "patch",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-prefetch-rec-10-not-missing-prescribed-opioids", LocalDate.now().minusDays(90).toString(),
                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        3, 1.0, "d", 1.0, "tablet",
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().toString(),
                        3, 30.0, "d"
                )

        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        prefetchEncounterId, patientId,
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().minusDays(90).toString()
                )
        );
        prefetchResources.put(
                "item4",
                generator.createObservation(
                        observationId, "10998-3", "Oxycodone [Presence] in Urine",
                        patientId, LocalDate.now().minusDays(28).toString(), "POS"
                )
        );

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": []\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_unprescribed_and_missing_opioids_test() {
        String patientId = "patient-rec-10-unprescribed-and-missing-opioids";
        String encounterId = "encounter-context-rec-10-unprescribed-and-missing-opioids";
        String prefetchEncounterId = "encounter-prefetch-rec-10-unprescribed-and-missing-opioids";
        String codeineObservationId = "observation-codeine-rec-10-unprescribed-and-missing-opioids";
        String oxyObservationId = "observation-oxy-rec-10-unprescribed-and-missing-opioids";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-10-unprescribed-and-missing-opioids-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-10-unprescribed-and-missing-opioids", patientId, encounterId,
                        "197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                        1, 12.0, "d", 1.0, "patch",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-prefetch-rec-10-unprescribed-and-missing-opioids", LocalDate.now().minusDays(90).toString(),
                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        3, 1.0, "d", 1.0, "tablet",
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().toString(),
                        3, 30.0, "d"
                )

        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        prefetchEncounterId, patientId,
                        LocalDate.now().minusDays(90).toString(), LocalDate.now().minusDays(90).toString()
                )
        );
        prefetchResources.put(
                "item4",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createObservation(
                                        codeineObservationId, "3507-1", "Codeine [Presence] in Urine",
                                        patientId, LocalDate.now().minusDays(21).toString(), "POS"
                                ),
                                generator.createObservation(
                                        oxyObservationId, "10998-3", "Oxycodone [Presence] in Urine",
                                        patientId, LocalDate.now().minusDays(28).toString(), "POS"
                                )
                        )
                )
        );

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Unprescribed Opioids Found In Urine Screening\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"Found the following unprescribed opioid(s): codeine\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://guidelines.gov/summaries/summary/50153/cdc-guideline-for-prescribing-opioids-for-chronic-pain---united-states-2016#420\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"summary\": \"Prescribed Opioids Not Found In Urine Screening\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"The following opioids are missing from the screening: oxycodone\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://guidelines.gov/summaries/summary/50153/cdc-guideline-for-prescribing-opioids-for-chronic-pain---united-states-2016#420\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_11_benzo_trigger_with_opioid_test() {
        String patientId = "patient-rec-11-benzo-trigger-with-opioid";
        String encounterId = "encounter-context-rec-11-benzo-trigger-with-opioid";
        String prefetchEncounterId = "encounter-prefetch-rec-11-benzo-trigger-with-opioid";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-11-benzo-trigger-with-opioid-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-11-benzo-trigger-with-opioid", patientId, encounterId,
                        "1298088", "Flurazepam Hydrochloride 15 MG Oral Capsule",
                        1, 1.0, "d", 1.0, "capsule",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-prefetch-rec-11-benzo-trigger-with-opioid", LocalDate.now().minusDays(28).toString(),
                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        3, 1.0, "d", 1.0, "tablet",
                        LocalDate.now().minusDays(28).toString(), LocalDate.now().plusMonths(3).toString(),
                        3, 30.0, "d"
                )

        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        prefetchEncounterId, patientId,
                        LocalDate.now().minusDays(28).toString(), LocalDate.now().minusDays(28).toString()
                )
        );

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-11-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Avoid prescribing opioid pain medication and benzodiazepine concurrently whenever possible.\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"The benzodiazepine prescription request is concurrent with an active opioid prescription\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_11_benzo_trigger_without_opioid_test() {
        String patientId = "patient-rec-11-benzo-trigger-without-opioid";
        String encounterId = "encounter-context-rec-11-benzo-trigger-without-opioid";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-11-benzo-trigger-without-opioid-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-11-benzo-trigger-without-opioid", patientId, encounterId,
                        "1298088", "Flurazepam Hydrochloride 15 MG Oral Capsule",
                        1, 1.0, "d", 1.0, "capsule",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-11-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": []\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_11_opioid_trigger_with_benzo_test() {
        String patientId = "patient-rec-11-opioid-trigger-with-benzo";
        String encounterId = "encounter-context-rec-11-opioid-trigger-with-benzo";
        String prefetchEncounterId = "encounter-prefetch-rec-11-opioid-trigger-with-benzo";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-11-opioid-trigger-with-benzo-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-11-opioid-trigger-with-benzo", patientId, encounterId,
                        "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        1, 1.0, "d", 1.0, "tablet",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-prefetch-rec-11-opioid-trigger-with-benzo", LocalDate.now().minusDays(28).toString(),
                        patientId, prefetchEncounterId, "1298088", "Flurazepam Hydrochloride 15 MG Oral Capsule",
                        3, 1.0, "d", 1.0, "capsule",
                        LocalDate.now().minusDays(28).toString(), LocalDate.now().plusMonths(3).toString(),
                        3, 30.0, "d"
                )

        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        prefetchEncounterId, patientId,
                        LocalDate.now().minusDays(28).toString(), LocalDate.now().minusDays(28).toString()
                )
        );

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-11-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Avoid prescribing opioid pain medication and benzodiazepine concurrently whenever possible.\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"The opioid prescription request is concurrent with an active benzodiazepine prescription\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_11_opioid_trigger_without_benzo_test() {
        String patientId = "patient-rec-11-opioid-trigger-without-benzo";
        String encounterId = "encounter-context-rec-11-opioid-trigger-without-benzo";

        JsonObject patient = generator.createDefaultPatient(patientId, "female");
        JsonObject encounter = generator.createEncounterToday(encounterId, patientId);

        generator.outputBasicRecBundle(patient, encounter, "rec-11-opioid-trigger-without-benzo-bundle.json");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Patient/" + patientId,
                generator.getAsString(patient)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu2Url + "/Encounter/" + encounterId,
                generator.getAsString(encounter)
        );

        List<JsonObject> contextResources = Collections.singletonList(
                generator.createContextMedicationOrder(
                        "order-context-rec-11-opioid-trigger-without-benzo", patientId, encounterId,
                        "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        1, 1.0, "d", 1.0, "tablet",
                        3, 30.0, "d"
                )
        );

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1", patient);

        String request = Dstu2OpioidRequestGenerator.createMedicationPrescribeRequest(
                baseDstu2Url, patientId, encounterId, contextResources, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-11-dstu2",
                request
        );

        String expected = "{\n" +
                "  \"cards\": []\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        TestHelper.batchDelete(
                baseDstu2Url + "/Encounter/" + encounterId,
                baseDstu2Url + "/Patient/" + patientId
        );
    }
}
