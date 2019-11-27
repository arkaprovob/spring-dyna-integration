package com.pintegration.config;

import com.pintegration.business.JsonTransformer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import com.pintegration.business.handler.Handler;


@Configuration
@ConditionalOnBean(DynamicPIntegration.class)
public class IntegrationConfig {

    @Bean
    public DirectChannel redisQueueHandler() {
        return new CustomMessageChannel();
    }


    @Bean // General example of handling a queue
    public RedisQueueMessageDrivenEndpoint consumerEndPoint(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        RedisQueueMessageDrivenEndpoint endPoint = new RedisQueueMessageDrivenEndpoint("Redis-Queue",
                connectionFactory);
        endPoint.setOutputChannelName("redisQueueHandler");
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }

    @Bean
    public IntegrationFlow flow(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        return IntegrationFlows.from("redisQueueHandler")
                .transform(JsonTransformer::transformIntoJson)
                .handle(x -> Handler.handle((JSONObject) x.getPayload()))
                .get();
    }

}
