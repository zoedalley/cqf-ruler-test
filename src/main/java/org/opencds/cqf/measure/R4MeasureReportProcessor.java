package org.opencds.cqf.measure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class R4MeasureReportProcessor extends MeasureReportProcessor {

    public R4MeasureReportProcessor(String measureReportString) {
        super(measureReportString);
    }

    @Override
    public JsonObject getGroupById(String id) {
        if (getMeasureReport().has("group")) {
            JsonArray groupArray = getMeasureReport().getAsJsonArray("group");
            for (JsonElement groupElement : groupArray) {
                JsonObject group = groupElement.getAsJsonObject();
                if (group.has("code")) {
                    JsonObject codeObj = group.getAsJsonObject("code");
                    if (codeObj.has("coding")) {
                        for (JsonElement codingElement : codeObj.getAsJsonArray("coding")) {
                            if (codingElement.getAsJsonObject().has("code")) {
                                if (codingElement.getAsJsonObject().getAsJsonPrimitive("code").getAsString().equals(id)) {
                                    return group;
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
