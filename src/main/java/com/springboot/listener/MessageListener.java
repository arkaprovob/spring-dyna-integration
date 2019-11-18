package com.springboot.listener;

import com.springboot.intfc.MessageGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.redis.outbound.RedisQueueOutboundChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    private MessageGateway messageGateway;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @ServiceActivator(inputChannel = "inputChannel", outputChannel = "redisChannel")
    public Message<?> receiveFromInputChannel(Message<?> message) {

        System.out.println("receieve from service");
        return message;
    }

    @ServiceActivator(inputChannel = "redisChannel")
    public void sendMessageToQueue(Message<?> message) {
        RedisQueueOutboundChannelAdapter adapter = new RedisQueueOutboundChannelAdapter("Redis-Queue", jedisConnectionFactory);
        adapter.handleMessage(message);
    }


    @ServiceActivator(inputChannel = "receiverChannel")
    public void receiveFromQueue(Message<?> message) {
        System.out.println("received from redis queue " + message);
    }


}
