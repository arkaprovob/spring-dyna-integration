package com.springboot.config;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;

/*
* there is a direct link between DirectChannel bean name
* and I/0 channel name this class is created to show that implementation.
 * */
public class CustomMessageChannel extends DirectChannel {

    @Override
    protected boolean doSend(Message<?> message, long timeout) {
        System.out.println("CustomMessageChannel doSend method invoked");
        return super.doSend(message,timeout);
    }

}
