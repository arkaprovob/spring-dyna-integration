package com.springboot.config;

import com.springboot.businesslogic.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;

@Configuration
@EnableIntegration
@IntegrationComponentScan("com.springboot")
public class IntegrationConfig {

    @Autowired
    Transformer transformer;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {

        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public DirectChannel redisQueueHandler() {
        return new CustomMessageChannel();
    }

    @Bean // General example of handling a queue
    public RedisQueueMessageDrivenEndpoint consumerEndPoint() {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("Redis-Queue",
                jedisConnectionFactory());
        endPoint.setOutputChannelName("redisQueueHandler");
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }

    @Bean //example of handling a queue using IntegrationFlows bean
    public RedisQueueMessageDrivenEndpoint anotherConsumerEndPoint() {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("Jedis-Queue",
                jedisConnectionFactory());
        endPoint.setOutputChannelName("out-channel");
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }

    @Bean //example of handling a queue using Filter
    public RedisQueueMessageDrivenEndpoint consumerEndPointWithFilterCriteria() {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("Kedis-Queue",
                jedisConnectionFactory());
        endPoint.setOutputChannelName("filterChannel");
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }

    @Bean //example of handling a queue using route
    public RedisQueueMessageDrivenEndpoint consumerEndPointWithRouting() {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("Ledis-Queue",
                jedisConnectionFactory());
        endPoint.setOutputChannelName("routeChannel");
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }


    @Bean
    public IntegrationFlow flow(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        return IntegrationFlows.from("out-channel")
                .handle(x -> System.out.println("message " + x + "handled"))
                .get();
    }


}
