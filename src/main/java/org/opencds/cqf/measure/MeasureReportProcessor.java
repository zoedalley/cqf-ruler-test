package org.opencds.cqf.measure;

import com.google.gson.*;

public abstract class MeasureReportProcessor {
    private JsonObject measureReport;

    public MeasureReportProcessor(String measureReportString) {
        measureReport = new GsonBuilder().create().fromJson(measureReportString, JsonObject.class);
    }

    public JsonObject getMeasureReport() {
        return measureReport;
    }

    public abstract JsonObject getGroupById(String id);

    public JsonObject getPopulationByCode(JsonObject group, String code) {
        if (group.has("population")) {
            JsonArray populations = group.getAsJsonArray("population");
            for (JsonElement populationElement : populations) {
                JsonObject population = populationElement.getAsJsonObject();
                if (population.has("code")) {
                    JsonObject codeObj = population.getAsJsonObject("code");
                    if (codeObj.has("coding")) {
                        JsonArray coding = codeObj.getAsJsonArray("coding");
                        for (JsonElement codingElement : coding) {
                            JsonObject codingObj = codingElement.getAsJsonObject();
                            if (codingObj.has("code") && codingObj.getAsJsonPrimitive("code").getAsString().equals(code)) {
                                return population;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
