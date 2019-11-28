package com.pintegration.config;

import com.pintegration.business.JsonTransformer;
import com.pintegration.config.examine.CustomRedisQueueMessageDrivenEndpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import com.pintegration.business.handler.Handler;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class PIntegration {

    Logger log = LoggerFactory.getLogger("PIntegration");

    @Autowired
    private IntegrationFlowContext integrationFlowContext;

    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfiguration.class);
    ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }


    private final  GenericApplicationContext context;

    public PIntegration(GenericApplicationContext context) {
        this.context = context;
    }



    private Set<String> listOfRedisQueue(){
        byte[] keyPattern = "*Queue".getBytes();
        Set<String> listOfQueues = new HashSet<>();
        Set<byte[]> preKeys = jedisConnectionFactory().getConnection().keys(keyPattern);
        assert preKeys != null;
        preKeys.forEach(queueName-> listOfQueues.add(new String(queueName, StandardCharsets.UTF_8)));
        log.info("listOfQueues are as follows {}",listOfQueues.toString());
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
        log.info("registered new DirectChannel named  {}",channel);
        context.registerBean(consumerEndPointBeanName,CustomRedisQueueMessageDrivenEndpoint.class,()->consumerEndPoint(queueName,channel));
        CustomRedisQueueMessageDrivenEndpoint endpoint = (CustomRedisQueueMessageDrivenEndpoint) context.getBean(consumerEndPointBeanName);
        log.info("registered new RedisQueueMessageDrivenEndpoint named  {}",consumerEndPointBeanName);
        IntegrationFlow flow = flow(channel);
        this.integrationFlowContext.registration(flow).register().start();
        log.info("registered new IntegrationFlowBean named  {}",integrationFlowBeanName);

    }


    private DirectChannel redisQueueHandler(String channel) {
        return new CustomMessageChannel(channel);
    }
    private CustomRedisQueueMessageDrivenEndpoint consumerEndPoint(String queueName,String channel) {
        CustomRedisQueueMessageDrivenEndpoint endPoint = new CustomRedisQueueMessageDrivenEndpoint(queueName,
                jedisConnectionFactory());
        endPoint.setOutputChannelName(channel);
        endPoint.setSerializer(new StringRedisSerializer());
        return endPoint;
    }
    private IntegrationFlow flow(String channel) {
        String chnl = channel; //"myapp."+channel
        log.info("registering for channel {}",chnl);
        return IntegrationFlows.from(chnl)
                .transform(JsonTransformer::transformIntoJson)
                .handle(x -> Handler.handle((JSONObject) x.getPayload()))
                .get();
    }

    @Configuration
    @EnableIntegration
    public static class RootConfiguration {

    }

}
