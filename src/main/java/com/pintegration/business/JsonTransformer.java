package com.pintegration.business;

import org.json.JSONObject;

public class JsonTransformer {

    public static JSONObject transformIntoJson(String message) {
        System.out.println("Transforming String to JSON");
        JSONObject payload = new JSONObject(message);
        if (!payload.has("eventCategory")) // if te key does not exists then add default
            payload.put("eventCategory", "provision");
        return payload;
    }
}
