package org.opencds.cqf.dstu2.cds.opioid;

import com.google.gson.JsonObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ErrorCollector;
import org.opencds.cqf.TestHelper;
import org.opencds.cqf.client.RequestFactory;
import org.opencds.cqf.dstu3.cds.opioid.Dstu3OpioidCdsTestSuite;
import org.opencds.cqf.gen.Dstu2OpioidRequestGenerator;
import org.opencds.cqf.gen.Dstu2OpioidTestDataGenerator;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;

public class Dstu2OpioidCdsPatientViewTestSuite {

    private static final String TERMINOLOGY_BUNDLE_FILE = "opioid_cds_terminology_bundle.json";
    private static final String FHIRHELPERS_LIBRARY_FILE = "library_FHIRHelpers.json";
    private static final String OMTKDATA_LIBRARY_FILE = "library_OMTKData.json";
    private static final String OMTKLOGIC_LIBRARY_FILE = "library_OMTKLogic.json";
    private static final String COMMON_LIBRARY_FILE = "library_OpioidCDSCommonDSTU2.json";

    private static final String REC_10_LIBRARY_FILE = "library_opioidcds_recommendation_10_dstu2_patient_view.json";
    private static final String REC_10_PLANDEF_FILE = "plandefinition_opioidcds_10_dstu2_patient_view.json";
    private static final String REC_11_LIBRARY_FILE = "library_opioidcds_recommendation_11_dstu2_patient_view.json";
    private static final String REC_11_PLANDEF_FILE = "plandefinition_opioidcds_11_dstu2_patient_view.json";

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
                baseDstu3Url + "/Library/opioidcds-recommendation-10-dstu2-patient-view",
                TestHelper.getResource(REC_10_LIBRARY_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/PlanDefinition/opioidcds-10-dstu2-patient-view",
                TestHelper.getResource(REC_10_PLANDEF_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );

        // Recommendation 11 artifacts
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/opioidcds-recommendation-11-dstu2-patient-view",
                TestHelper.getResource(REC_11_LIBRARY_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/PlanDefinition/opioidcds-11-dstu2-patient-view",
                TestHelper.getResource(REC_11_PLANDEF_FILE, Dstu2OpioidCdsMedicationPrescribeTestSuite.class)
        );
    }

    //@AfterClass
    public static void destroy() {
        TestHelper.delete(baseDstu3Url + "/PlanDefinition/opioidcds-10-dstu2-patient-view");
        TestHelper.delete(baseDstu3Url + "/Library/opioidcds-recommendation-10-dstu2-patient-view");
        TestHelper.delete(baseDstu3Url + "/PlanDefinition/opioidcds-11-dstu2-patient-view");
        TestHelper.delete(baseDstu3Url + "/Library/opioidcds-recommendation-11-dstu2-patient-view");

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
    public void rec_10_patient_view_end_of_life_med_exclusion_test() {
        String patientId = "patient-rec-10-end-of-life-patient-view";
        String encounterId = "encounter-context-rec-10-end-of-life-patient-view";

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1",
                generator.createDefaultPatient(patientId, "female")
        );
        prefetchResources.put("item2",
                generator.createEncounter(
                        encounterId, patientId, LocalDate.now().minusDays(28).toString(),
                        LocalDate.now().minusDays(28).toString()
                )
        );
        prefetchResources.put("item3",
                generator.createMedicationOrder(
                        "order-rec-10-end-of-life-patient-view", LocalDate.now().minusDays(28).toString(), patientId, encounterId,
                        "1012727", "Carbinoxamine maleate 0.4 MG/ML / Hydrocodone Bitartrate 1 MG/ML / Pseudoephedrine Hydrochloride 6 MG/ML Oral Solution",
                        1, 12.0, "d", 5.0, "mL", LocalDate.now().minusDays(28).toString(),
                        LocalDate.now().plusDays(62).toString(),3, 30.0, "d"
                )
        );
        String request = Dstu2OpioidRequestGenerator.createPatientViewRequest(
                baseDstu2Url, patientId, encounterId, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2-patient-view",
                request
        );
        String expected = "{\n" + "  \"cards\": []\n" + "}\n";
        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_patient_view_illicit_drugs_test() {
        String patientId = "patient-rec-10-illicit-drugs-patient-view";
        String encounterId = "encounter-context-rec-10-illicit-drugs-patient-view";
        String prefetchEncounterId = "encounter-prefetch-rec-10-illicit-drugs-patient-view";
        String observationId = "observation-rec-10-illicit-drugs-patient-view";

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1",
                generator.createDefaultPatient(patientId, "female")
        );
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-oxy-rec-10-illicit-drugs-patient-view", LocalDate.now().minusDays(60).toString(),
                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                        3, 1.0, "d", 1.0, "tablet",
                        LocalDate.now().minusDays(60).toString(), LocalDate.now().plusDays(30).toString(),
                        3, 30.0, "d"
                )
        );
        prefetchResources.put(
                "item3",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createEncounter(
                                        encounterId, patientId,
                                        LocalDate.now().minusDays(70).toString(), LocalDate.now().minusDays(70).toString()
                                ),
                                generator.createEncounter(
                                        prefetchEncounterId, patientId,
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().minusDays(60).toString()
                                )
                        )
                )
        );
        prefetchResources.put(
                "item4",
                generator.createObservation(
                        observationId, "3426-4", "Tetrahydrocannabinol [Presence] in Urine",
                        patientId, LocalDate.now().minusDays(28).toString(), "POS"
                )
        );
        prefetchResources.put(
                "item5",
                generator.createMedicationOrder(
                        "order-fentanyl-rec-10-illicit-drugs-patient-view", LocalDate.now().minusDays(70).toString(),
                        patientId, encounterId,"197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                        1, 12.0, "d", 1.0, "patch",
                        LocalDate.now().minusDays(70).toString(), LocalDate.now().plusDays(20).toString(),3, 30.0, "d"
                )
        );
        String request = Dstu2OpioidRequestGenerator.createPatientViewRequest(
                baseDstu2Url, patientId, encounterId, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2-patient-view", request
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
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_patient_view_no_screenings_test() {
        String patientId = "patient-rec-10-no-screenings-patient-view";
        String encounterId = "encounter-fentanyl-rec-10-no-screenings-patient-view";
        String prefetchEncounterId = "encounter-oxy-prefetch-rec-10-no-screenings-patient-view";

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1",
                generator.createDefaultPatient(patientId, "female")
        );
        prefetchResources.put(
                "item2",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createMedicationOrder(
                                        "order-fentanyl-rec-10-illicit-drugs-patient-view", LocalDate.now().minusDays(70).toString(),
                                        patientId, encounterId,"197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                                        1, 12.0, "d", 1.0, "patch",
                                        LocalDate.now().minusDays(70).toString(), LocalDate.now().plusDays(20).toString(),3, 30.0, "d"
                                ),
                                generator.createMedicationOrder(
                                        "order-oxy-rec-10-no-screenings-patient-view", LocalDate.now().minusDays(90).toString(),
                                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                                        3, 1.0, "d", 1.0, "tablet",
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().plusDays(30).toString(),
                                        3, 30.0, "d"
                                )
                        )
                )
        );
        prefetchResources.put(
                "item3",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createEncounter(
                                        encounterId, patientId,
                                        LocalDate.now().minusDays(70).toString(), LocalDate.now().minusDays(70).toString()
                                ),
                                generator.createEncounter(
                                        prefetchEncounterId, patientId,
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().minusDays(60).toString()
                                )
                        )
                )
        );

        String request = Dstu2OpioidRequestGenerator.createPatientViewRequest(
                baseDstu2Url, patientId, encounterId, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2-patient-view", request
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
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_patient_view_not_missing_prescribed_opioids_test() {
        String patientId = "patient-rec-10-not-missing-prescribed-opioids-patient-view";
        String encounterId = "encounter-fentanyl-rec-10-not-missing-prescribed-opioids-patient-view";
        String prefetchEncounterId = "encounter-oxy-rec-10-not-missing-prescribed-opioids-patient-view";
        String observationId = "observation-rec-10-not-missing-prescribed-opioids-patient-view";

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1",
                generator.createDefaultPatient(patientId, "female")
        );
        prefetchResources.put(
                "item2",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createMedicationOrder(
                                        "order-fentanyl-rec-10-not-missing-prescribed-opioids-patient-view", LocalDate.now().minusDays(20).toString(),
                                        patientId, encounterId,"197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                                        1, 12.0, "d", 1.0, "patch",
                                        LocalDate.now().minusDays(20).toString(), LocalDate.now().plusDays(70).toString(),3, 30.0, "d"
                                ),
                                generator.createMedicationOrder(
                                        "order-oxy-rec-10-not-missing-prescribed-opioids-patient-view", LocalDate.now().minusDays(90).toString(),
                                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                                        3, 1.0, "d", 1.0, "tablet",
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().plusDays(30).toString(),
                                        3, 30.0, "d"
                                )
                        )
                )
        );
        prefetchResources.put(
                "item3",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createEncounter(
                                        encounterId, patientId,
                                        LocalDate.now().minusDays(20).toString(), LocalDate.now().minusDays(20).toString()
                                ),
                                generator.createEncounter(
                                        prefetchEncounterId, patientId,
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().minusDays(60).toString()
                                )
                        )
                )
        );
        prefetchResources.put(
                "item4",
                generator.createObservation(
                        observationId, "10998-3", "Oxycodone [Presence] in Urine",
                        patientId, LocalDate.now().minusDays(28).toString(), "POS"
                )
        );

        String request = Dstu2OpioidRequestGenerator.createPatientViewRequest(
                baseDstu2Url, patientId, encounterId, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2-patient-view",
                request
        );

        String expected = "{\n" +
                "  \"cards\": []\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_10_patient_view_unprescribed_and_missing_opioids_test() {
        String patientId = "patient-rec-10-unprescribed-and-missing-opioids-patient-view";
        String encounterId = "encounter-fentanyl-rec-10-unprescribed-and-missing-opioids-patient-view";
        String prefetchEncounterId = "encounter-oxy-rec-10-unprescribed-and-missing-opioids-patient-view";
        String codeineObservationId = "observation-codeine-rec-10-unprescribed-and-missing-opioids-patient-view";
        String oxyObservationId = "observation-oxy-rec-10-unprescribed-and-missing-opioids-patient-view";

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1",
                generator.createDefaultPatient(patientId, "female")
        );
        prefetchResources.put(
                "item2",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createMedicationOrder(
                                        "order-fentanyl-rec-10-unprescribed-and-missing-opioids-patient-view", LocalDate.now().minusDays(20).toString(),
                                        patientId, encounterId,"197696", "72 HR Fentanyl 0.075 MG/HR Transdermal System",
                                        1, 12.0, "d", 1.0, "patch",
                                        LocalDate.now().minusDays(20).toString(), LocalDate.now().plusDays(70).toString(),3, 30.0, "d"
                                ),
                                generator.createMedicationOrder(
                                        "order-oxy-rec-10-unprescribed-and-missing-opioids-patient-view", LocalDate.now().minusDays(90).toString(),
                                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                                        3, 1.0, "d", 1.0, "tablet",
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().plusDays(30).toString(),
                                        3, 30.0, "d"
                                )
                        )
                )
        );
        prefetchResources.put(
                "item3",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createEncounter(
                                        encounterId, patientId,
                                        LocalDate.now().minusDays(20).toString(), LocalDate.now().minusDays(20).toString()
                                ),
                                generator.createEncounter(
                                        prefetchEncounterId, patientId,
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().minusDays(60).toString()
                                )
                        )
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

        String request = Dstu2OpioidRequestGenerator.createPatientViewRequest(
                baseDstu2Url, patientId, encounterId, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-10-dstu2-patient-view",
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
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_11_patient_view_concurrent_test() {
        String patientId = "patient-rec-11-concurrent-patient-view";
        String encounterId = "encounter-benzo-rec-11-concurrent-patient-view";
        String prefetchEncounterId = "encounter-opioid-rec-11-concurrent-patient-view";

         Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1",
                generator.createDefaultPatient(patientId, "female")
        );
        prefetchResources.put(
                "item2",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createMedicationOrder(
                                        "order-benzo-rec-11-concurrent-patient-view", LocalDate.now().minusDays(60).toString(),
                                        patientId, encounterId, "1298088", "Flurazepam Hydrochloride 15 MG Oral Capsule",
                                        1, 1.0, "d", 1.0, "capsule",
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().plusDays(30).toString(),3, 30.0, "d"
                                ),
                                generator.createMedicationOrder(
                                        "order-opioid-rec-11-concurrent-patient-view", LocalDate.now().minusDays(28).toString(),
                                        patientId, prefetchEncounterId, "1049502", "12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet",
                                        3, 1.0, "d", 1.0, "tablet",
                                        LocalDate.now().minusDays(28).toString(), LocalDate.now().plusMonths(3).toString(),
                                        3, 30.0, "d"
                                )
                        )
                )
        );
        prefetchResources.put(
                "item3",
                generator.createBundle(
                        null,
                        Arrays.asList(
                                generator.createEncounter(
                                        encounterId, patientId,
                                        LocalDate.now().minusDays(60).toString(), LocalDate.now().minusDays(60).toString()
                                ),
                                generator.createEncounter(
                                        prefetchEncounterId, patientId,
                                        LocalDate.now().minusDays(28).toString(), LocalDate.now().minusDays(28).toString()
                                )
                        )
                )
        );

        String request = Dstu2OpioidRequestGenerator.createPatientViewRequest(
                baseDstu2Url, patientId, encounterId, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-11-dstu2-patient-view",
                request
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Patient has active prescriptions for opioid pain medication and benzodiazepines\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"Avoid prescribing opioid pain medication and benzodiazepines concurrently whenever possible\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));
    }

    @Test
    @Category(Dstu2OpioidCdsTests.class)
    public void rec_11_patient_view_not_concurrent_test() {
        String patientId = "patient-rec-11-not-concurrent-patient-view";
        String encounterId = "encounter-benzo-rec-11-not-concurrent-patient-view";

        Map<String, JsonObject> prefetchResources = new HashMap<>();
        prefetchResources.put("item1",
                generator.createDefaultPatient(patientId, "female")
        );
        prefetchResources.put(
                "item2",
                generator.createMedicationOrder(
                        "order-benzo-rec-11-not-concurrent-patient-view", LocalDate.now().minusDays(60).toString(),
                        patientId, encounterId, "1298088", "Flurazepam Hydrochloride 15 MG Oral Capsule",
                        1, 1.0, "d", 1.0, "capsule",
                        LocalDate.now().minusDays(60).toString(), LocalDate.now().plusDays(30).toString(),3, 30.0, "d"
                )
        );
        prefetchResources.put(
                "item3",
                generator.createEncounter(
                        encounterId, patientId,
                        LocalDate.now().minusDays(60).toString(), LocalDate.now().minusDays(60).toString()
                )
        );

        String request = Dstu2OpioidRequestGenerator.createPatientViewRequest(
                baseDstu2Url, patientId, encounterId, prefetchResources
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu2Url + "/opioidcds-11-dstu2-patient-view",
                request
        );

        String expected = "{\n" +
                "  \"cards\": []\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));
    }
}
