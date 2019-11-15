package com.springboot.listener;

import com.springboot.intfc.MessageGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class MessageListener {

    Logger log  = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    private MessageGateway messageGateway;

    @ServiceActivator(inputChannel = "abc",outputChannel = "out-channel")
    public Message<?>  receiveFromInputChannel(Message<?> message) {

        System.out.println("message object  is "+message);
        System.out.println("received from inputChannel channel " + message.getPayload() + " Thread : " + Thread.currentThread().getName());
        return newMessage("Data successfuly transformed");
    }



    @ServiceActivator(inputChannel = "out-channel",outputChannel = "")
    public Message<?> receiveFromOutputChannel(Message<?> message) {
        System.out.println("INSIDE out-channel...");
        System.out.println("received from outChannel " + message.getPayload() + " Thread : " + Thread.currentThread().getName());
        return message;
    }

    private <S> Message<S> newMessage(S paylaod){
        return new Message<S>() {
            @Override
            public S getPayload() {
                return paylaod;
            }

            @Override
            public MessageHeaders getHeaders() {
                HashMap map = new HashMap();
                map.put("custom-header","secret");
                return new MessageHeaders(map);
            }
        };
    }
}
