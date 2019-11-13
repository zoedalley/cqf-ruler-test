package org.opencds.cqf.measure;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.opencds.cqf.testcase.GroupItems;
import org.opencds.cqf.testcase.MeasureTestScript;

public abstract class MeasureEvaluationTestBase {

    public abstract void processMeasureScore(JsonObject group, GroupItems items);

    protected void processExpectedResponse(MeasureTestScript script, MeasureReportProcessor processor) {
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
                JsonObject denominatorExclusion = processor.getPopulationByCode(group, "denominator-exclusion");
                if (denominatorExclusion.has("count")) {
                    Assert.assertTrue(denominatorExclusion.get("count").getAsBigInteger().equals(items.getDenominatorExclusion()));
                }
            }
            if (items.getMeasureScore() != null) {
                processMeasureScore(group, items);
            }
        }
    }
}
