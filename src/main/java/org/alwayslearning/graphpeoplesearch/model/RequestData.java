package org.alwayslearning.graphpeoplesearch.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestData {
    private List<Request> requests;
}
