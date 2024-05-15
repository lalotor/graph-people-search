package org.alwayslearning.graphpeoplesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
  @JsonProperty("value")
  private List<Value> value;

  @JsonProperty("@odata.context")
  private String odataContext;

}
