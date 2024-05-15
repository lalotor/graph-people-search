package org.alwayslearning.graphpeoplesearch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Hit {
  private String hitId;
  private int rank;
  private String summary;
  private Resource resource;
}
