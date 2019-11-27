package com.pintegration.web;

import com.pintegration.config.DynamicPIntegration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/refresh")
@RestController
public class ExecutionController {


    private final DynamicPIntegration dynamicPIntegration;

    public ExecutionController(DynamicPIntegration dynamicPIntegration) {
       this.dynamicPIntegration = dynamicPIntegration;
    }

    @GetMapping
    public String fire(){
        dynamicPIntegration.createOnTheFlyIntegrationBean();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DynamicPIntegration.class);
        context.refresh();
        return "...";
    }

}
