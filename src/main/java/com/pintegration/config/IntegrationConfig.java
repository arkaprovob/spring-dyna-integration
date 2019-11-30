package com.pintegration.config;

import com.pintegration.business.JsonTransformer;
import com.pintegration.business.handler.Handler;
import com.pintegration.config.examine.CustomRedisQueueMessageDrivenEndpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;


@Configuration
@ConditionalOnBean(DynamicIntegration.class)
public class IntegrationConfig {

    Logger log = LoggerFactory.getLogger("IntegrationConfig");

    @Bean
    public DirectChannel redisQueueHandler() {
        return new CustomMessageChannel();
    }


    @Bean // General example of handling a queue
    public CustomRedisQueueMessageDrivenEndpoint consumerEndPoint(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        CustomRedisQueueMessageDrivenEndpoint endPoint = new CustomRedisQueueMessageDrivenEndpoint("Redis-Queue",
                connectionFactory);
        endPoint.setOutputChannelName("redisQueueHandler");
        endPoint.setSerializer(new StringRedisSerializer());
        log.info("\n");
        log.info("CustomRedisQueueMessageDrivenEndpoint IS RUNNING STATUS ......{}", endPoint.isRunning());
        log.info("CustomRedisQueueMessageDrivenEndpoint IS LISTENING STATUS ......{}", endPoint.isListening());
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
