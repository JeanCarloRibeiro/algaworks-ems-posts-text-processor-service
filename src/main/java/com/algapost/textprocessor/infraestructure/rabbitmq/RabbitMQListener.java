package com.algapost.textprocessor.infraestructure.rabbitmq;


import com.algapost.textprocessor.api.model.PostData;
import com.algapost.textprocessor.domain.service.TextProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.algapost.textprocessor.infraestructure.rabbitmq.RabbitMQConfig.QUEUE_TEXT_PROCESSOR;


@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

  private final TextProcessorService service;

  @SneakyThrows
  @RabbitListener(queues = QUEUE_TEXT_PROCESSOR, concurrency = "2-3")
  public void handleProcessTemperature(@Payload PostData postData,
                     @Headers Map<String, Object> headers) {

    log.info("Received Post data {} to process from Queue {}.", postData, RabbitMQConfig.QUEUE_TEXT_PROCESSOR);
    service.processText(postData);
  }

}
