package org.opencds.cqf.measure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class MeasureReportProcessor {
    private JsonObject measureReport;
    private Gson gson = new GsonBuilder().create();

    public MeasureReportProcessor(String measureReportString) {
        measureReport = gson.fromJson(measureReportString, JsonObject.class);
    }

    public JsonObject getMeasureReport() {
        return measureReport;
    }

    public Gson getGson() {
        return gson;
    }

    public abstract JsonObject getGroupById(String id);
    public abstract JsonObject getPopulationByCode(JsonObject group, String code);
}
