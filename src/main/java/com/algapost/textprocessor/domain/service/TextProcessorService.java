package com.algapost.textprocessor.domain.service;

import com.algapost.textprocessor.api.model.PostData;
import com.algapost.textprocessor.api.model.PostDataResult;
import com.algapost.textprocessor.domain.service.exception.PostServiceException;
import com.algapost.textprocessor.infraestructure.rabbitmq.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextProcessorService {

  public static final double ESTIMATED_VALUE = 0.10;

  private final RabbitTemplate rabbitTemplate;

  public void processText(PostData postData) {
    try {
      PostDataResult post = PostDataResult.builder()
              .postId(postData.getPostId())
              .wordCount(getWordCount(postData.getPostBody()))
              .calculatedValue(getCalculatedValue(postData.getPostBody()))
              .build();

      rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_POST_RESULT, "", post);
      log.info("Sent Post Result {} to {}.", post, RabbitMQConfig.QUEUE_POST_RESULT);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PostServiceException(e);
    }

  }

  private BigDecimal getCalculatedValue (String input){
    int wordCount = getWordCount(input);
    return BigDecimal.valueOf(ESTIMATED_VALUE * wordCount);
  }

  private static int getWordCount (String input){
    return input.length();
  }

}
