package com.springboot.listener;

import com.springboot.intfc.MessageGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@MessageEndpoint
public class RoutingHandler {

    @Autowired
    private MessageGateway messageGateway;

    @ServiceActivator(inputChannel = "yooChannel")
    public void receiveYooMessages(Message<?> message) {
        System.out.println("yoo message received " + message);
    }

    @ServiceActivator(inputChannel = "booChannel")
    public void receiveBooMessages(Message<?> message) {
        System.out.println("boo message received " + message);
    }

}
