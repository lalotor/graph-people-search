package org.alwayslearning.graphpeoplesearch.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
  private List<String> entityTypes;
  private Query query;
}
