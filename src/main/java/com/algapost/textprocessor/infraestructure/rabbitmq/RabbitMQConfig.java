package com.algapost.textprocessor.infraestructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

  private static final String TEXT_PROCESSOR = "text-processor-service.post-processing.v1";
  private static final String POST_RESULT = "post-service.post-processing-result.v1";
  public static final String FANOUT_EXCHANGE_TEXT_PROCESSOR = TEXT_PROCESSOR + ".e";
  public static final String FANOUT_EXCHANGE_POST_RESULT = POST_RESULT + ".e";
  public static final String QUEUE_TEXT_PROCESSOR = TEXT_PROCESSOR + ".q";
  public static final String QUEUE_POST_RESULT = POST_RESULT + ".q";
  public static final String DEAD_LETTER_QUEUE_TEXT_PROCESSOR = TEXT_PROCESSOR + ".dlq";
  public static final String DEAD_LETTER_POST_RESULT = POST_RESULT + ".dlq";

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
    return new Jackson2JsonMessageConverter(objectMapper);
  }
  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
    return new RabbitAdmin(factory);
  }

  public FanoutExchange textProcessorExchange() {
    return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE_TEXT_PROCESSOR).build();
  }

  public FanoutExchange postProcessingResultExchange() {
    return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE_POST_RESULT).build();
  }

  @Bean
  public Queue queueTextProcessor() {
    Map<String, Object> ars = new HashMap<>();
    ars.put("x-dead-letter-exchange", "");
    ars.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_TEXT_PROCESSOR);
    return QueueBuilder.durable(QUEUE_TEXT_PROCESSOR)
            .withArguments(ars)
            .build();
  }

  @Bean
  public Queue queuePostProcessingResult() {
    Map<String, Object> ars = new HashMap<>();
    ars.put("x-dead-letter-exchange", "");
    ars.put("x-dead-letter-routing-key", DEAD_LETTER_POST_RESULT);
    return QueueBuilder.durable(QUEUE_POST_RESULT).withArguments(ars).build();
  }

  @Bean
  public Binding bindingTextProcessor() {
    return BindingBuilder.bind(queueTextProcessor()).to(textProcessorExchange());
  }

  @Bean
  public Binding bindingPostProcessingResult() {
    return BindingBuilder.bind(queuePostProcessingResult()).to(postProcessingResultExchange());
  }

  @Bean
  public Queue deadLetterQueueTextProcessor() {
    return QueueBuilder.durable(DEAD_LETTER_QUEUE_TEXT_PROCESSOR).build();
  }

  @Bean
  public Queue deadLetterQueuePostResult() {
    return QueueBuilder.durable(DEAD_LETTER_POST_RESULT).build();
  }

}
