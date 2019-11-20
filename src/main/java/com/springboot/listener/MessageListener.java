package com.springboot.listener;

import com.springboot.intfc.MessageGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.redis.outbound.RedisQueueOutboundChannelAdapter;
import org.springframework.messaging.Message;

@MessageEndpoint
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


    @ServiceActivator(inputChannel = "redisQueueHandler")
    public void receiveFromQueue(Message<?> message) {
        System.out.println("received from redis queue " + message);
    }

    @Filter(inputChannel = "filterChannel", outputChannel = "processFilteredMessage", discardChannel = "messageDiscardChannel")
    public boolean receiveFromQueueWIthRespectToMessageFilter(Message<?> message) {
        return message.getPayload().equals("HII");
    }

    @ServiceActivator(inputChannel = "processFilteredMessage")
    public void receiveFilteredOutput(Message<?> message) {
        System.out.println("filter message received " + message);
    }

    @ServiceActivator(inputChannel = "messageDiscardChannel")
    public void receiveUnFilteredOutput(Message<?> message) {
        System.out.println("discarded message received " + message);
    }

/*    @Router(inputChannel = "routeChannel")
    public String routeMessageToAChannel(Message<?> message) {
        if (message.getPayload().toString().equals("Yoo")) {
            return "yooChannel";
        }
        return "booChannel";
    }*/


    @Router(inputChannel = "routeChannel",channelMappings = {"Yoo=yooChannel"},
            defaultOutputChannel = "booChannel",resolutionRequired = "false")
    public String routeMessageToAChannel(Message<?> message) {
        return message.getPayload().toString();
    }


    @ServiceActivator(inputChannel = "ThrowErrorChannel")
    public void receiveErroneousMessages(Message<?> message) {
        throw new RuntimeException("Intentional");
    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleError(Message<?> message) {
        System.out.println("handling error of  " + message);
        System.out.println("Sending back to the queue from where it came.");
    }

}
