package org.opencds.cqf.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RequestHelper {

    private Gson gson;

    public RequestHelper() {
        gson = new Gson();
    }

    public JsonElement getJsonProperty(String rawJson, String property) {
        JsonElement element = gson.fromJson(rawJson, JsonObject.class).get(property);
        if (element == null) {
            throw new RuntimeException("Unable to resolve property: " + property);
        }
        return element;
    }
}
