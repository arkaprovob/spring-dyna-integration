package com.pintegration.business.handler;

import com.pintegration.processor.Processor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Handler {

    private static Logger log = LoggerFactory.getLogger("Handler");
    private static Map<String, String> router = new HashMap<>();

    static {
        router.put("provision", "com.pintegration.processor.ProvisioningProcessor");
        router.put("statusEvent", "com.pintegration.processor.StatusEventProcessor");
    }

    private Handler() {
    }

    public static void handle(JSONObject payload) {
        log.info("Handling payload and sending for further processing");

        String determineProcessorValue = payload.getString("eventCategory");
        String processorClassName = router.get(determineProcessorValue);
        Processor processor = null;
        try {
            if(Objects.isNull(processorClassName) || processorClassName.isEmpty()){
                processor = payload1 -> log
                        .info("eventCategory attribute not found for this {} payload", payload1);
            }else{
                processor = (Processor) Class.forName(processorClassName).newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            log.error("", e);
        }
        Assert.notNull(processor, "processor not found!");
        processor.process(payload);
    }


}
