package com.pintegration.processor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvisioningProcessor implements Processor {
    private static Logger log = LoggerFactory.getLogger("ProvisioningProcessor");

    @Override
    public void process(JSONObject payload) {
        log.info("Processing incoming events of type "+this.getClass());
    }
}
