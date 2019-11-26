package com.pintegration.business.handler;

import com.pintegration.processor.Processor;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Handler {

    private Handler(){}

    private static Map<String,String > router = new HashMap<>();

    static{
        router.put("provision","com.pintegration.processor.ProvisioningProcessor");
        router.put("statusEvent","com.pintegration.processor.StatusEventProcessor");
    }

    public static void handle(JSONObject payload){
        System.out.println("Handling payload and sending for further processing");

        String determineProcessorValue = payload.getString("eventCategory");
        String processorClassName = router.get(determineProcessorValue);

        Processor processor = null;
        try {
            processor = (Processor)Class.forName(processorClassName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        processor.process(payload);
    }




}
