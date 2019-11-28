package com.pintegration.web;

import com.pintegration.config.PIntegration;
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

    private final PIntegration PIntegration;

    public ExecutionController(PIntegration PIntegration) {
       this.PIntegration = PIntegration;
    }

    @GetMapping
    public String fire(){
        PIntegration.createOnTheFlyIntegrationBean();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(PIntegration.class);
        context.refresh();
        return "...";
    }

}
