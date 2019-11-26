package com.springboot.business.handler;

import java.util.HashMap;
import java.util.Map;

public class Handler {

    private static Map router = new HashMap();

    static{
        router.put("provision","com.springboot.processor.ProvisioningProcessor");
        router.put("statusEvent","com.springboot.processor.StatusEventProcessor");
    }




}
