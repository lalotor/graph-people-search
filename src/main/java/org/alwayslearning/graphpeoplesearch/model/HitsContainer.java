package org.alwayslearning.graphpeoplesearch.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HitsContainer {
  private List<Hit> hits;
  private int total;
  private boolean moreResultsAvailable;
}
