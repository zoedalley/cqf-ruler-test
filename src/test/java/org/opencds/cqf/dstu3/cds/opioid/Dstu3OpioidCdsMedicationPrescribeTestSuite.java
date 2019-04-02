package org.opencds.cqf.dstu3.cds.opioid;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ErrorCollector;
import org.opencds.cqf.TestHelper;
import org.opencds.cqf.client.RequestFactory;

import static org.hamcrest.Matchers.equalTo;

public class Dstu3OpioidCdsMedicationPrescribeTestSuite
{
    private static final String TERMINOLOGY_BUNDLE_FILE = "opioid_cds_terminology_bundle.json";
    private static final String FHIRHELPERS_LIBRARY_FILE = "library_FHIRHelpers.json";
    private static final String OMTKDATA_LIBRARY_FILE = "library_OMTKData.json";
    private static final String OMTKLOGIC_LIBRARY_FILE = "library_OMTKLogic.json";
    private static final String COMMON_LIBRARY_FILE = "library_OpioidCDSCommonDSTU3.json";

    private static final String REC_4_BUNDLE_FILE = "bundle_opioidcds_recommendation_04_dstu3.json";
    private static final String REC_5_BUNDLE_FILE = "bundle_opioidcds_recommendation_05_dstu3.json";
    private static final String REC_7_BUNDLE_FILE = "bundle_opioidcds_recommendation_07_dstu3.json";
    private static final String REC_8_BUNDLE_FILE = "bundle_opioidcds_recommendation_08_dstu3.json";
    private static final String REC_10_BUNDLE_FILE = "bundle_opioidcds_recommendation_10_dstu3.json";
    private static final String REC_11_BUNDLE_FILE = "bundle_opioidcds_recommendation_11_dstu3.json";

    private static String baseDstu3Url;
    private static String cdsDstu3Url;

    @BeforeClass
    public static void setup()
    {
        baseDstu3Url = System.getProperty("base.dstu3");
        cdsDstu3Url = System.getProperty("cds.dstu3");

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(TERMINOLOGY_BUNDLE_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/FHIRHelpers",
                TestHelper.getResource(FHIRHELPERS_LIBRARY_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/OMTKData",
                TestHelper.getResource(OMTKDATA_LIBRARY_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/OMTKLogic",
                TestHelper.getResource(OMTKLOGIC_LIBRARY_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Library/OpioidCDSCommonSTU3",
                TestHelper.getResource(COMMON_LIBRARY_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(REC_4_BUNDLE_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(REC_5_BUNDLE_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(REC_7_BUNDLE_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(REC_8_BUNDLE_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(REC_10_BUNDLE_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                baseDstu3Url,
                TestHelper.getResource(REC_11_BUNDLE_FILE, Dstu3OpioidCdsMedicationPrescribeTestSuite.class)
        );
    }

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    @Category(Dstu3OpioidCdsTests.class)
    public void rec_4_long_acting_opioid_test()
    {
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/example-rec-04-long-acting-opioid",
                TestHelper.getResource("patient_rec_04_long_acting_opioid.json", this.getClass())
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Encounter/example-rec-04-long-acting-opioid-context",
                TestHelper.getResource("encounter_rec_04_long_acting_opioid_context.json", this.getClass())
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu3Url + "/opioidcds-04",
                TestHelper.getResource("request_example_rec_04_long_acting_opioid.json", this.getClass())
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Recommend use of immediate-release opioids instead of extended release/long acting opioids when starting patient on opioids.\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"The following medication requests(s) release rates should be re-evaluated: 12 HR Oxycodone Hydrochloride 10 MG Extended Release Oral Tablet\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        // TODO: cleanup
    }

    @Test
    @Category(Dstu3OpioidCdsTests.class)
    public void rec_5_mme_greater_than_fifty_test()
    {
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/example-rec-05-mme-greater-than-fifty",
                TestHelper.getResource("patient_rec_05_mme_greater_than_fifty.json", this.getClass())
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Encounter/example-rec-05-mme-greater-than-fifty-context",
                TestHelper.getResource("encounter_rec_05_mme_greater_than_fifty_context.json", this.getClass())
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu3Url + "/opioidcds-05",
                TestHelper.getResource("request_example_rec_05_mme_greater_than_fifty.json", this.getClass())
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"High risk for opioid overdose - taper now\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"Total morphine milligram equivalent (MME) is 179.99999820 \\u0027mg/d\\u0027. Taper to less than 50.\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      },\n" +
                "      \"links\": [\n" +
                "        {\n" +
                "          \"label\": \"MME Conversion Tables\",\n" +
                "          \"url\": \"https://www.cdc.gov/drugoverdose/pdf/calculating_total_daily_dose-a.pdf\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        // TODO: cleanup
    }

    @Test
    @Category(Dstu3OpioidCdsTests.class)
    public void rec_7_seven_of_past_ten_test()
    {
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/example-rec-07-seven-of-past-ten-days",
                TestHelper.getResource("patient_rec_07_seven_of_past_ten_days.json", this.getClass())
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Encounter/example-rec-07-seven-of-past-ten-days-context",
                TestHelper.getResource("encounter_rec_07_seven_of_past_ten_days_context.json", this.getClass())
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu3Url + "/opioidcds-07",
                TestHelper.getResource("request_example_rec_07_seven_of_past_ten_days.json", this.getClass())
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Patients on opioid therapy should be evaluated for benefits and harms within 1 to 4 weeks of starting opioid therapy and every 3 months or more subsequently.\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"No evaluation for benefits and harms has been performed for the patient starting opioid therapy\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      },\n" +
                "      \"suggestions\": [\n" +
                "        {\n" +
                "          \"label\": \"Assessment of risk for opioid use procedure\",\n" +
                "          \"actions\": [\n" +
                "            {\n" +
                "              \"type\": \"create\",\n" +
                "              \"description\": \"No evaluation for benefits and harms has been performed for the patient starting opioid therapy\",\n" +
                "              \"resource\": {\n" +
                "                \"resourceType\": \"ProcedureRequest\",\n" +
                "                \"status\": \"draft\",\n" +
                "                \"intent\": \"order\",\n" +
                "                \"code\": {\n" +
                "                  \"coding\": [\n" +
                "                    {\n" +
                "                      \"system\": \"http://snomed.info/sct\",\n" +
                "                      \"code\": \"454281000124100\",\n" +
                "                      \"display\": \"Assessment of risk for opioid abuse (procedure)\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"subject\": {\n" +
                "                  \"reference\": \"Patient/example-rec-07-seven-of-past-ten-days\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\"id\":.*\\s", "").replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        // TODO: cleanup
    }

    @Test
    @Category(Dstu3OpioidCdsTests.class)
    public void rec_8_mme_greater_than_fifty_test()
    {
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/example-rec-08-mme-greater-than-fifty",
                TestHelper.getResource("patient_rec_08_mme_greater_than_fifty.json", this.getClass())
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Encounter/example-rec-08-mme-greater-than-fifty-context",
                TestHelper.getResource("encounter_rec_08_mme_greater_than_fifty_context.json", this.getClass())
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu3Url + "/opioidcds-08",
                TestHelper.getResource("request_example_rec_08_mme_greater_than_fifty.json", this.getClass())
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Incorporate into the management plan strategies to mitigate risk; including considering offering naloxone when factors that increase risk for opioid overdose are present\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"Consider offering naloxone given following risk factor(s) for opioid overdose: Average MME (54.000000 \\u0027mg/d\\u0027) \\u003e\\u003d 50 mg/day.\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      },\n" +
                "      \"links\": [\n" +
                "        {\n" +
                "          \"label\": \"MME Conversion Tables\",\n" +
                "          \"url\": \"https://www.cdc.gov/drugoverdose/pdf/calculating_total_daily_dose-a.pdf\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        // TODO: cleanup
    }

    @Test
    @Category(Dstu3OpioidCdsTests.class)
    public void rec_10_illicit_drugs_test()
    {
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/example-rec-10-illicit-drugs",
                TestHelper.getResource("patient_rec_10_illicit_drugs.json", this.getClass())
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Encounter/example-rec-10-illicit-drugs-context",
                TestHelper.getResource("encounter_rec_10_illicit_drugs_context.json", this.getClass())
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu3Url + "/opioidcds-10",
                TestHelper.getResource("request_example_rec_10_illicit_drugs.json", this.getClass())
        );

        String expected = "{\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"summary\": \"Illicit Drugs Found In Urine Screening\",\n" +
                "      \"indicator\": \"warning\",\n" +
                "      \"detail\": \"Found the following illicit drug(s) in urine drug screen: Tetrahydrocannabinol\",\n" +
                "      \"source\": {\n" +
                "        \"label\": \"CDC guideline for prescribing opioids for chronic pain\",\n" +
                "        \"url\": \"https://www.cdc.gov/mmwr/volumes/65/rr/rr6501e1.htm?CDC_AA_refVal\\u003dhttps%3A%2F%2Fwww.cdc.gov%2Fmmwr%2Fvolumes%2F65%2Frr%2Frr6501e1er.htm\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        // TODO: cleanup
    }

    @Test
    @Category(Dstu3OpioidCdsTests.class)
    public void rec_11_benzo_trigger_with_opioid_test()
    {
        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Patient/example-rec-11-benzo-trigger-with-opioid",
                TestHelper.getResource("patient_rec_11_benzo_trigger_with_opioids.json", this.getClass())
        );

        RequestFactory.makeRequest(
                RequestFactory.RequestType.PUT,
                baseDstu3Url + "/Encounter/example-rec-11-benzo-trigger-with-opioid-context",
                TestHelper.getResource("encounter_rec_11_benzo_trigger_with_opioid_context.json", this.getClass())
        );

        String response = RequestFactory.makeRequest(
                RequestFactory.RequestType.POST,
                cdsDstu3Url + "/opioidcds-11",
                TestHelper.getResource("request_example_rec_11_benzo_trigger_with_opioid.json", this.getClass())
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
                "}\n";

        collector.checkThat(response.replaceAll("\\s+", ""), equalTo(expected.replaceAll("\\s+", "")));

        // TODO: cleanup
    }
}
