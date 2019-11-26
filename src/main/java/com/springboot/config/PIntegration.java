package com.springboot.config;


import com.springboot.business.JsonTransformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
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
public class PIntegration {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
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

    @Bean
    public IntegrationFlow flow(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        return IntegrationFlows.from("redisQueueHandler")
                .transform(JsonTransformer::transformIntoJson)
                .handle(x -> System.out.println("message " + x + "handled"))
                .get();
    }

}
