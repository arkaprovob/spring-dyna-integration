package com.springboot.intfc;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.concurrent.Future;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "inputChannel")
    public <S> String sendMessageToChannel1(S request);

}
