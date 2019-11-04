package org.opencds.cqf.dstu3.measure;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.opencds.cqf.TestHelper;
import org.opencds.cqf.client.RequestFactory;
import org.opencds.cqf.measure.Stu3MeasureReportProcessor;
import org.opencds.cqf.testcase.GroupItems;
import org.opencds.cqf.testcase.MeasureTestScript;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stu3MeasureEvaluationTests {

    private static String baseStu3Url;
    private static List<MeasureTestScript> scripts;

    @BeforeClass
    public static void setup() throws JAXBException {
        baseStu3Url = System.getProperty("base.dstu3");
        scripts = new ArrayList<>();
        JAXBContext jaxbContext = JAXBContext.newInstance(MeasureTestScript.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        TestHelper.fetchFiles(new File("src/test/resources/org/opencds/cqf/dstu3/measure/scripts"), f -> {
            try {
                scripts.add((MeasureTestScript) unmarshaller.unmarshal(f));
            } catch (JAXBException e) {
                System.out.println("Error unmarshalling file: " + f.getName());
                e.printStackTrace();
            }
        });
    }

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void MeasureTests() throws JAXBException, IOException {

        for (MeasureTestScript script : scripts) {
            TestHelper.loadMeasureData(script, baseStu3Url);
            String response =
                    RequestFactory.makeRequest(
                            RequestFactory.RequestType.GET,
                            baseStu3Url + TestHelper.buildEvaluateMeasureRequest(script),
                            null
                    );

            Stu3MeasureReportProcessor processor =  new Stu3MeasureReportProcessor(response);
            for (GroupItems items : script.getTest().getExpectedResponse().getGroup()) {
                if (items.getId() == null) continue;
                JsonObject group = processor.getGroupById(items.getId());
                if (items.getInitialPopulation() != null) {
                    JsonObject initialPopulation = processor.getPopulationByCode(group, "initial-population");
                    if (initialPopulation.has("count")) {
                        Assert.assertTrue(initialPopulation.get("count").getAsBigInteger().equals(items.getInitialPopulation()));
                    }
                }
                if (items.getDenominator() != null) {
                    JsonObject denominator = processor.getPopulationByCode(group, "denominator");
                    if (denominator.has("count")) {
                        Assert.assertTrue(denominator.get("count").getAsBigInteger().equals(items.getDenominator()));
                    }
                }
                if (items.getNumerator() != null) {
                    JsonObject numerator = processor.getPopulationByCode(group, "numerator");
                    if (numerator.has("count")) {
                        Assert.assertTrue(numerator.get("count").getAsBigInteger().equals(items.getNumerator()));
                    }
                }
                if (items.getDenominatorExclusion() != null) {
                    JsonObject denominatorExclusiion = processor.getPopulationByCode(group, "denominator-exclusion");
                    if (denominatorExclusiion.has("count")) {
                        Assert.assertTrue(denominatorExclusiion.get("count").getAsBigInteger().equals(items.getDenominatorExclusion()));
                    }
                }
                if (items.getMeasureScore() != null) {
                    if (group.has("measureScore")) {
                        Assert.assertTrue(group.getAsJsonPrimitive("measureScore").getAsBigDecimal().equals(items.getMeasureScore()));
                    }
                }
            }
        }
    }
}
