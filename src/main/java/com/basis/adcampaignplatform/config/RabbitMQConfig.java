package com.basis.adcampaignplatform.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange campaignExchange() {
        return new TopicExchange("campaign-exchange");
    }

    @Bean
    public Queue campaignQueue() {
        return new Queue("campaign-queue", true);
    }

    @Bean
    public Binding campaignBinding(Queue campaignQueue,
                                   TopicExchange campaignExchange) {
        return BindingBuilder
                .bind(campaignQueue)
                .to(campaignExchange)
                .with("campaign.*");
    }
}