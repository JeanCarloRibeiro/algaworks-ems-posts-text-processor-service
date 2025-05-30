package com.algapost.textprocessor.api.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class PostDataResult {
  private String postId;
  private Integer wordCount;
  private BigDecimal calculatedValue;
}
