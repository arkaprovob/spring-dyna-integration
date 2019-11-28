package com.pintegration.processor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusEventProcessor implements Processor{

    private static Logger log = LoggerFactory.getLogger("StatusEventProcessor");

    @Override
    public void process(JSONObject payload) {
        log.info("Processing incoming events of type "+this.getClass());
    }
}
