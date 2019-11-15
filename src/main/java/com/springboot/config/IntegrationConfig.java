package com.springboot.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.springboot.utility.JsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.annotation.IntegrationComponentScan;
import com.springboot.businesslogic.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.redis.inbound.RedisQueueInboundGateway;
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
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(){

        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public DirectChannel receiverChannel() {
        return new DirectChannel();
    }

    @Bean
    public RedisQueueMessageDrivenEndpoint consumerEndPoint() {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("Redis-Queue",
                jedisConnectionFactory());
        endPoint.setOutputChannelName("receiverChannel");
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }

}
