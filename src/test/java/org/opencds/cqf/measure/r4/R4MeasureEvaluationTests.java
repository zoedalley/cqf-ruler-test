package org.opencds.cqf.measure.r4;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.opencds.cqf.TestHelper;
import org.opencds.cqf.client.RequestFactory;
import org.opencds.cqf.measure.MeasureEvaluationTestBase;
import org.opencds.cqf.measure.R4MeasureReportProcessor;
import org.opencds.cqf.testcase.GroupItems;
import org.opencds.cqf.testcase.MeasureTestScript;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class R4MeasureEvaluationTests extends MeasureEvaluationTestBase {
    private static String baseR4Url;
    private static List<MeasureTestScript> scripts;

    @BeforeClass
    public static void setup() throws JAXBException {
        baseR4Url = System.getProperty("base.r4");
        scripts = new ArrayList<>();
        JAXBContext jaxbContext = JAXBContext.newInstance(MeasureTestScript.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        TestHelper.fetchFiles(new File("src/test/resources/org/opencds/cqf/r4/measure/scripts"), f -> {
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
    public void MeasureTests() throws IOException {
        for (MeasureTestScript script : scripts) {
            if (script.isEnabled()) {
                TestHelper.loadMeasureData(script, baseR4Url);
                for (MeasureTestScript.Test test : script.getTest()) {
                    String response =
                            RequestFactory.makeRequest(
                                    RequestFactory.RequestType.GET,
                                    baseR4Url + TestHelper.buildEvaluateMeasureRequest(test),
                                    null
                            );
                    R4MeasureReportProcessor processor = new R4MeasureReportProcessor(response);
                    processExpectedResponse(test, processor);
                }
            }
        }
    }

    @Override
    public void processMeasureScore(String assertionMessage, JsonObject group, GroupItems items) {
        if (group.has("measureScore")) {
            JsonObject measureScoreQuantity = group.getAsJsonObject("measureScore");
            if (measureScoreQuantity.has("value")) {
                Assert.assertEquals(assertionMessage, measureScoreQuantity.getAsJsonPrimitive("value").getAsBigDecimal(), items.getMeasureScore());
            }
        }
    }
}
