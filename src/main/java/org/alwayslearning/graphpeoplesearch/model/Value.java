package org.alwayslearning.graphpeoplesearch.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Value {
  @JsonProperty("hitsContainers")
  private List<HitsContainer> hitsContainers;
}
