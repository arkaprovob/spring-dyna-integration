package com.pintegration.processor;

import org.json.JSONObject;

public interface Processor {

    void process(JSONObject payload);

}
