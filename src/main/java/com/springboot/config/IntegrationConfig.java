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
    public RedisQueueMessageDrivenEndpoint consumerEndPoint(RedisConnectionFactory redisConnectionFactory,RedisSerializer<?> serializer) {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("pre.tenant.targetsystem",
                redisConnectionFactory);
        endPoint.setOutputChannelName("abc");
        //endPoint.setErrorChannelName("errorChannelName");
        endPoint.setSerializer(new GenericJackson2JsonRedisSerializer());
        return endPoint;
    }


    @Bean
    public RedisQueueMessageDrivenEndpoint consumerEndPoint2(RedisConnectionFactory redisConnectionFactory,RedisSerializer<?> serializer) {
        Jackson2JsonRedisSerializer<? extends JsonNode> slizer = new Jackson2JsonRedisSerializer<>(JsonNode.class);
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("Redis-Queue2",
                redisConnectionFactory);
        endPoint.setOutputChannelName("abc");
        endPoint.setSerializer(slizer);
        return endPoint;
    }

/*   @Bean
    public IntegrationFlow flow(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        return IntegrationFlows.from("out-channel")
                .handle(x -> System.out.println("message handled"))
                .get();
    }*/


/*    @Bean
    public IntegrationFlow flow() {
        return IntegrationFlows.from("abc")
                .transform(transformer,"transform")
                .handle(x-> System.out.println(x.getPayload()))
                .get();
    }*/

}
