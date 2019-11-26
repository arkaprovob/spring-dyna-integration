package com.pintegration.config;

import com.pintegration.business.JsonTransformer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import com.pintegration.business.handler.Handler;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.Arrays;

@Configuration
public class DynamicPIntegration {

    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private GenericApplicationContext context;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        jedisConnectionFactory = new JedisConnectionFactory();
        return jedisConnectionFactory;
    }



    public String[] listOfRedisQueue(){
        return new String[]{"Jedis-Queue"};
    }

    void createOnTheFlyIntegrationBean(){
        Arrays.asList(listOfRedisQueue()).forEach(entry->{
            registerBeans(entry);
        });
    }

    private void registerBeans(String queueName){

        String channel = "channel-"+queueName;
        String consumerEndPointBeanName = queueName+"-"+channel+"ConsumerEndPoint";
        String integrationFlowBeanName = channel+"IntegrationFlow";

        context.registerBean(channel,DirectChannel.class,()->redisQueueHandler(channel));
        context.registerBean(consumerEndPointBeanName,RedisQueueMessageDrivenEndpoint.class,()->consumerEndPoint(queueName,channel));
        context.registerBean(integrationFlowBeanName,IntegrationFlow.class,()->flow(channel));

    }


    public DirectChannel redisQueueHandler(String channel) {
        return new CustomMessageChannel(channel);
    }
    public RedisQueueMessageDrivenEndpoint consumerEndPoint(String queueName,String channel) {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint(queueName,
                jedisConnectionFactory);
        endPoint.setOutputChannelName(channel);
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }
    public IntegrationFlow flow(String channel) {
        return IntegrationFlows.from(channel)
                .transform(JsonTransformer::transformIntoJson)
                .handle(x -> Handler.handle((JSONObject) x.getPayload()))
                .get();
    }

}
