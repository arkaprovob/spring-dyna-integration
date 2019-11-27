package com.pintegration.config;

import com.pintegration.business.JsonTransformer;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import com.pintegration.business.handler.Handler;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Configuration
/*@EnableIntegration*/
public class DynamicPIntegration {


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }


    private final  GenericApplicationContext context;

    public DynamicPIntegration(GenericApplicationContext context) {
        this.context = context;
    }



    private Set<String> listOfRedisQueue(){
        byte[] keyPattern = "*Queue".getBytes();
        Set<String> listOfQueues = new HashSet<>();
        Set<byte[]> preKeys = jedisConnectionFactory().getConnection().keys(keyPattern);
        assert preKeys != null;
        preKeys.forEach(queueName-> listOfQueues.add(new String(queueName, StandardCharsets.UTF_8)));
        System.out.println("listOfQueues are as follows "+listOfQueues.toString());
        return listOfQueues;
    }

    public void createOnTheFlyIntegrationBean(){
        listOfRedisQueue().forEach(this::registerBeans);
    }

    private void registerBeans(String queueName){

        String channel = "channel-"+queueName;
        String consumerEndPointBeanName = queueName+"-"+channel+"ConsumerEndPoint";
        String integrationFlowBeanName = channel+"IntegrationFlow";

        context.registerBean(channel,DirectChannel.class,()->redisQueueHandler(channel));
        System.out.println("registered new DirectChannel named  "+channel);
        context.registerBean(consumerEndPointBeanName,RedisQueueMessageDrivenEndpoint.class,()->consumerEndPoint(queueName,channel));
        System.out.println("registered new RedisQueueMessageDrivenEndpoint named  "+consumerEndPointBeanName);
        context.registerBean(integrationFlowBeanName,IntegrationFlow.class,()->flow(channel));
        System.out.println("registered new RedisQueueMessageDrivenEndpoint named  "+integrationFlowBeanName);

    }


    private DirectChannel redisQueueHandler(String channel) {
        return new CustomMessageChannel(channel);
    }
    private RedisQueueMessageDrivenEndpoint consumerEndPoint(String queueName,String channel) {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint(queueName,
                jedisConnectionFactory());
        endPoint.setOutputChannelName(channel);
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }
    private IntegrationFlow flow(String channel) {
        return IntegrationFlows.from(channel)
                .transform(JsonTransformer::transformIntoJson)
                .handle(x -> Handler.handle((JSONObject) x.getPayload()))
                .get();
    }

}
