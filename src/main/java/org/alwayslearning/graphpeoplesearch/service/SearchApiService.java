package org.alwayslearning.graphpeoplesearch.service;

import lombok.extern.slf4j.Slf4j;
import org.alwayslearning.graphpeoplesearch.exception.ResourceNotFoundException;
import org.alwayslearning.graphpeoplesearch.model.RequestData;
import org.alwayslearning.graphpeoplesearch.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SearchApiService {

  private final RestTemplate restTemplate;
  @Value("${api.url}")
  private String apiUrl;

  public SearchApiService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public SearchResult fetchDataFromApi(String accessToken, RequestData requestData) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, accessToken);
    headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    String queryString = requestData.getRequests().get(0).getQuery().getQueryString();
    log.debug("queryString param: {}", queryString);

    HttpEntity<RequestData> requestEntity = new HttpEntity<>(requestData, headers);
    ResponseEntity<SearchResult> response = restTemplate.postForEntity(apiUrl, requestEntity, SearchResult.class);
    SearchResult searchResult = response.getBody();

    if (searchResult != null && searchResult.getValue().get(0).getHitsContainers().get(0).getTotal() == 0) {
      throw new ResourceNotFoundException("User " + queryString + " not found");
    }

    return searchResult;
  }

}