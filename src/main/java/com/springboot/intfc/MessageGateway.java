package com.springboot.intfc;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.concurrent.Future;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "transForm")
    public <S> Future<S> sendMessageToChannel1(S request);

}
