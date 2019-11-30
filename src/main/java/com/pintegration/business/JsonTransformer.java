package com.pintegration.business;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTransformer {
    private JsonTransformer(){}
    private static Logger log = LoggerFactory.getLogger("JsonTransformer");

    public static JSONObject transformIntoJson(String message) {
        log.info("Transforming String to JSON");
        JSONObject payload;
        try{
            payload = new JSONObject(message);
        }catch(JSONException e){
            log.error("unable to convert to JSON, building custom json with paylaod");
            payload = new JSONObject().put("value",message);
        }


        if (!payload.has("eventCategory")) // if te key does not exists then add default
            payload.put("eventCategory", "provision");
        return payload;
    }
}
