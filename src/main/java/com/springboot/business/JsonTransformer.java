package com.springboot.business;

import org.json.JSONObject;
import org.springframework.messaging.Message;

public class JsonTransformer {

    public static JSONObject transformIntoJson(String message) {
        System.out.println("Transforming String to JSON");
        JSONObject payload = new JSONObject(message);
        if (!payload.has("eventCategory")) // if te key does not exists then add default
            payload.put("eventCategory", "provision");
        return payload;
    }
}
