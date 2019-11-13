package com.springboot.listener;

import com.springboot.intfc.MessageGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageListener {

    @Autowired
    private MessageGateway messageGateway;

    @ServiceActivator(inputChannel = "transForm", outputChannel = "outChannel")
    public Message<?> receiveFromInputChannel(Message<?> message) {
        //Error simulation...
        if (message.getPayload().toString().equalsIgnoreCase("Hello payload-5")) {
            System.out.println("Input Output error occurred");
            try {
                throw new IOException("I/O exception occured");
            } catch (IOException e) {
                messageGateway.sendMessageToChannel1("After Hello payload-5 resolved");
            }

        }
        System.out.println("received from inputChannel channel " + message.getPayload() + " Thread : " + Thread.currentThread().getName());

        return new Message<Object>() {
            @Override
            public Object getPayload() {
                return message.getPayload() + " transformed";
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        };
    }

    @ServiceActivator(inputChannel = "outChannel", outputChannel = "someOtherChannel")
    public Message<?> receiveFromOutputChannel(Message<?> message) {
        System.out.println("received from outChannel " + message.getPayload() + " Thread : " + Thread.currentThread().getName());
        return message;
    }
}
