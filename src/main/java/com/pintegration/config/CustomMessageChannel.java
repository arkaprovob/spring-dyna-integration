package com.pintegration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;

import java.util.UUID;

/*
* there is a direct link between DirectChannel bean name
* and I/0 channel name this class is created to show that implementation.
 * */
public class CustomMessageChannel extends DirectChannel {

    private static Logger log = LoggerFactory.getLogger("CustomMessageChannel");

    private final String channelName;

    CustomMessageChannel(String channelName){
        super();
        this.channelName = channelName;
    }

    CustomMessageChannel(){
        super();
        this.channelName = UUID.randomUUID().toString();
    }

    @Override
    protected boolean doSend(Message<?> message, long timeout) {
        log.info("CustomMessageChannel doSend method invoked from object {}",channelName);
        return super.doSend(message,timeout);
    }

    public String assignedChannelName(){
        return this.channelName;
    }

}
