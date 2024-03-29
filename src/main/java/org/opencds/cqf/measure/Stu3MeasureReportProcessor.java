package org.opencds.cqf.measure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Stu3MeasureReportProcessor extends MeasureReportProcessor {

    public Stu3MeasureReportProcessor(String measureReportString) {
        super(measureReportString);
    }

    @Override
    public JsonObject getGroupById(String id) {
        if (getMeasureReport().has("group")) {
            JsonArray groupArray = getMeasureReport().getAsJsonArray("group");
            for (JsonElement groupElement : groupArray) {
                JsonObject group = groupElement.getAsJsonObject();
                if (group.has("identifier")) {
                    JsonElement identifierValue = group.getAsJsonObject("identifier").get("value");
                    if (identifierValue == null) continue;
                    if (identifierValue.getAsJsonPrimitive().getAsString().equals(id)) {
                        return group;
                    }
                }
            }
        }

        return null;
    }
}
