package org.alwayslearning.graphpeoplesearch.controller;

import lombok.extern.slf4j.Slf4j;
import org.alwayslearning.graphpeoplesearch.exception.UnauthorizedAccessException;
import org.alwayslearning.graphpeoplesearch.model.SearchResult;
import org.alwayslearning.graphpeoplesearch.service.SearchApiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.alwayslearning.graphpeoplesearch.util.Utils.getRequestData;
import static org.alwayslearning.graphpeoplesearch.util.Utils.isValidAuthorization;

@RestController
@RequestMapping("/api")
@Slf4j
public class SearchController {
  private final SearchApiService searchApiService;

  public SearchController(SearchApiService searchApiService) {
    this.searchApiService = searchApiService;
  }

  @GetMapping(value = "/search/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<SearchResult> searchData(@PathVariable("name") String name,
                                                 @RequestHeader("Authorization") String accessToken) {
    log.debug("name: {}", name);
    if (!isValidAuthorization(accessToken)) {
      throw new UnauthorizedAccessException("Invalid or missing authorization token");
    }

    return ResponseEntity.ok(searchApiService.fetchDataFromApi(accessToken, getRequestData(name)));
  }

}
