package com.pintegration.business;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTransformer {
    private static Logger log = LoggerFactory.getLogger("JsonTransformer");

    public static JSONObject transformIntoJson(String message) {
        log.info("Transforming String to JSON");
        JSONObject payload = new JSONObject(message);
        if (!payload.has("eventCategory")) // if te key does not exists then add default
            payload.put("eventCategory", "provision");
        return payload;
    }
}
