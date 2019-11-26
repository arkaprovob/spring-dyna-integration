package com.pintegration.processor;

import org.json.JSONObject;

public class ProvisioningProcessor implements Processor {
    @Override
    public void process(JSONObject payload) {
        System.out.println("Processing incoming events of type "+this.getClass());
    }
}
