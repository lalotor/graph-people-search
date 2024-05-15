package org.alwayslearning.graphpeoplesearch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EmailAddress {
  private String address;
  private double rank;
}
