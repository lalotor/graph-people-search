package org.alwayslearning.graphpeoplesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alwayslearning.graphpeoplesearch.exception.ResourceNotFoundException;
import org.alwayslearning.graphpeoplesearch.model.RequestData;
import org.alwayslearning.graphpeoplesearch.model.SearchResult;
import org.alwayslearning.graphpeoplesearch.service.SearchApiService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraphPeopleSearchApplicationTests {

  private static final String NAME = "Elkin Torres";
  private static final String TOKEN = "Bearer 123abc";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  @Autowired
  TestRestTemplate restTemplate;
  @Value("classpath:user_found.json")
  Resource userFoundFile;
  @Value("classpath:user_not_found.json")
  Resource userNotFoundFile;
  @MockBean
  private SearchApiService service;
  private HttpHeaders headers;
  private String userFoundJson;

  @BeforeEach
  void setup() throws IOException {
    userFoundJson = userFoundFile.getContentAsString(Charset.defaultCharset());
    SearchResult searchResult = OBJECT_MAPPER.readValue(userFoundJson, SearchResult.class);
    given(service.fetchDataFromApi(any(String.class), any(RequestData.class))).willReturn(searchResult);
    headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
  }

  @Test
  void givenExistentPerson_whenSearching_thenSuccessfulResult() throws IOException, JSONException {
    //given - precondition or setup

    // when - action or the behaviour that we are going to test
    ResponseEntity<SearchResult> response = restTemplate.exchange("/api/search/" + NAME, HttpMethod.GET,
        new HttpEntity<>(headers), SearchResult.class);

    // then - verify the output
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    SearchResult returnedSearchResult = response.getBody();
    assertThat(returnedSearchResult).isNotNull();
    String actualJson = OBJECT_MAPPER.writeValueAsString(returnedSearchResult);
    JSONAssert.assertEquals(userFoundJson, actualJson, true);
  }

  @Test
  void givenNonExistentPerson_whenSearching_thenNotFoundResult() throws IOException {
    //given - precondition or setup
    given(service.fetchDataFromApi(any(String.class), any(RequestData.class)))
        .willThrow(ResourceNotFoundException.class);

    // when - action or the behaviour that we are going to test
    ResponseEntity<SearchResult> response = restTemplate.exchange("/api/search/NonExistentName", HttpMethod.GET,
        new HttpEntity<>(headers), SearchResult.class);

    // then - verify the output
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void givenInvalidAuthorization_whenSearching_thenUnauthorizedResult() {
    //given - precondition or setup
    headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "InvalidToken");

    // when - action or the behaviour that we are going to test
    ResponseEntity<SearchResult> response = restTemplate.exchange("/api/search/" + NAME, HttpMethod.GET,
        new HttpEntity<>(headers), SearchResult.class);

    // then - verify the output
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void givenEmptyAuthorization_whenSearching_thenUnauthorizedResult() {
    //given - precondition or setup

    // when - action or the behaviour that we are going to test
    ResponseEntity<SearchResult> response = restTemplate.exchange("/api/search/" + NAME, HttpMethod.GET,
        null, SearchResult.class);

    // then - verify the output
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

}
