package com.springboot.intfc;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "inputChannel")
    public <S> String sendMessageToChannel1(S request);

}
