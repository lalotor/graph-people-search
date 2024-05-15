package org.alwayslearning.graphpeoplesearch.util;

import org.alwayslearning.graphpeoplesearch.model.Query;
import org.alwayslearning.graphpeoplesearch.model.Request;
import org.alwayslearning.graphpeoplesearch.model.RequestData;

import java.util.List;

public class Utils {

  public static final String BEARER = "Bearer";

  private Utils() {
  }

  public static RequestData getRequestData(String name) {
    Request request = Request.builder()
        .query(Query.builder().queryString(name).build())
        .entityTypes(List.of("person"))
        .build();

    return RequestData.builder()
        .requests(List.of(request))
        .build();
  }

  public static boolean isValidAuthorization(String authorizationToken) {
    return authorizationToken != null && authorizationToken.startsWith(BEARER);
  }
}
