package com.springboot.processor;

import org.json.JSONObject;

public interface Processor {

    void process(JSONObject payload);

}
