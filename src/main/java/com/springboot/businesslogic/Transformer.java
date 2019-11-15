package com.springboot.businesslogic;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Transformer {

    public Message<String> transform(Message<?> message){
        return newMessage("Thrid level of transformation");
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
