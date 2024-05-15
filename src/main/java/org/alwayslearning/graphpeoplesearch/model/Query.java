package org.alwayslearning.graphpeoplesearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Query {
    private String queryString;
}
