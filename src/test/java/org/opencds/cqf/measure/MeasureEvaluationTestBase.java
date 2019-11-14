package org.opencds.cqf.measure;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.opencds.cqf.testcase.GroupItems;
import org.opencds.cqf.testcase.MeasureTestScript;

public abstract class MeasureEvaluationTestBase {

    public abstract void processMeasureScore(JsonObject group, GroupItems items);

    protected void processExpectedResponse(MeasureTestScript.Test test, MeasureReportProcessor processor) {
        for (GroupItems items : test.getExpectedResponse().getGroup()) {
            if (items.getId() == null) continue;
            JsonObject group = processor.getGroupById(items.getId());
            if (items.getInitialPopulation() != null) {
                JsonObject initialPopulation = processor.getPopulationByCode(group, "initial-population");
                if (initialPopulation.has("count")) {
                    Assert.assertEquals(initialPopulation.get("count").getAsBigInteger(), items.getInitialPopulation());
                }
            }
            if (items.getDenominator() != null) {
                JsonObject denominator = processor.getPopulationByCode(group, "denominator");
                if (denominator.has("count")) {
                    Assert.assertEquals(denominator.get("count").getAsBigInteger(), items.getDenominator());
                }
            }
            if (items.getNumerator() != null) {
                JsonObject numerator = processor.getPopulationByCode(group, "numerator");
                if (numerator.has("count")) {
                    Assert.assertEquals(numerator.get("count").getAsBigInteger(), items.getNumerator());
                }
            }
            if (items.getDenominatorExclusion() != null) {
                JsonObject denominatorExclusion = processor.getPopulationByCode(group, "denominator-exclusion");
                if (denominatorExclusion.has("count")) {
                    Assert.assertEquals(denominatorExclusion.get("count").getAsBigInteger(), items.getDenominatorExclusion());
                }
            }
            if (items.getMeasureScore() != null) {
                processMeasureScore(group, items);
            }
        }
    }
}
