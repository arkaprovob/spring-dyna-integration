package com.pintegration.web;

import com.pintegration.config.DynamicIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/refresh")
@RestController
public class ExecutionController {

    private static Logger log = LoggerFactory.getLogger("ExecutionController");

    private final DynamicIntegration DynamicIntegration;

    public ExecutionController(DynamicIntegration DynamicIntegration) {
        this.DynamicIntegration = DynamicIntegration;
    }

    @GetMapping
    public String fire() {
        return DynamicIntegration.createOnTheFlyIntegrationBean();
    }

}
