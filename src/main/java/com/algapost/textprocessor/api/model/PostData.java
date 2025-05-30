package com.algapost.textprocessor.api.model;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PostData {
  private String postId;
  private String postBody;
}
