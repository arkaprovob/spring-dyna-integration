package com.springboot.controller;

import com.springboot.intfc.MessageGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulate")
public class EventSimulator {

    @Autowired
    private MessageGateway messageGateway;


    @GetMapping
    public void trigger() {
        for (int i = 0; i < 100; i++) {
            System.out.println("sending from " + Thread.currentThread().getName());
            messageGateway.sendMessageToChannel1("Hello payload-" + i);
        }

    }

    @PostMapping
    public void trigger(@RequestBody String payload) {
        messageGateway.sendMessageToChannel1(payload);
    }
}
