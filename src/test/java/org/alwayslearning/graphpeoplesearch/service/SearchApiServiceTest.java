package org.alwayslearning.graphpeoplesearch.service;

import org.alwayslearning.graphpeoplesearch.exception.ResourceNotFoundException;
import org.alwayslearning.graphpeoplesearch.model.Hit;
import org.alwayslearning.graphpeoplesearch.model.RequestData;
import org.alwayslearning.graphpeoplesearch.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.alwayslearning.graphpeoplesearch.util.Utils.getRequestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(SearchApiService.class)
class SearchApiServiceTest {
  private static final String NAME = "Elkin Torres";
  private static final String TOKEN = "Bearer 123abc";
  @Autowired
  private SearchApiService searchApiService;
  @Autowired
  private MockRestServiceServer mockRestServiceServer;
  @Value("classpath:user_found.json")
  Resource userFoundFile;
  @Value("classpath:user_not_found.json")
  Resource userNotFoundFile;
  private RequestData requestData;

  @BeforeEach
  public void setup() {
    requestData = getRequestData(NAME);
  }

  @Test
  void givenExistentPerson_whenFetchData_thenSuccessfulResult() throws IOException {
    //given - precondition or setup
    this.mockRestServiceServer
        .expect(requestTo("https://graph.microsoft.com/beta/search/query"))
        .andRespond(withSuccess(userFoundFile.getContentAsString(Charset.defaultCharset()), MediaType.APPLICATION_JSON));

    // when - action or the behaviour that we are going to test
    SearchResult searchResult = searchApiService.fetchDataFromApi(TOKEN, requestData);

    // then - verify the output
    assertThat(searchResult).isNotNull();
    assertThat(searchResult.getValue()).hasSize(1);
    assertThat(searchResult.getValue().get(0).getHitsContainers()).hasSize(1);
    assertThat(searchResult.getValue().get(0).getHitsContainers().get(0).getHits()).hasSize(2);
    Hit hit1 = searchResult.getValue().get(0).getHitsContainers().get(0).getHits().get(0);
    assertThat(hit1.getHitId()).isEqualTo("abc-123");
    assertThat(hit1.getRank()).isEqualTo(1);
    assertThat(hit1.getResource()).isNotNull();
    assertThat(hit1.getResource().getDisplayName()).isEqualTo("John Doe");
    assertThat(hit1.getResource().getEmailAddresses()).hasSize(2);
    assertThat(hit1.getResource().getOdataType()).isEqualTo("#microsoft.graph.person");
    Hit hit2 = searchResult.getValue().get(0).getHitsContainers().get(0).getHits().get(1);
    assertThat(hit2.getHitId()).isEqualTo("xyz-987");
    assertThat(hit2.getRank()).isEqualTo(2);
    assertThat(hit2.getResource()).isNotNull();
    assertThat(hit2.getResource().getEmailAddresses()).hasSize(1);
    assertThat(hit2.getResource().getEmailAddresses().get(0).getAddress()).isEqualTo("johndoe@gmail.com");
  }

  @Test
  void givenNonExistentPerson_whenFetchData_thenNotFoundResult() throws IOException {
    //given - precondition or setup
    this.mockRestServiceServer
        .expect(requestTo("https://graph.microsoft.com/beta/search/query"))
        .andRespond(withSuccess(userNotFoundFile.getContentAsString(Charset.defaultCharset()), MediaType.APPLICATION_JSON));

    // when - action or the behaviour that we are going to test
    assertThrows(ResourceNotFoundException.class, () -> searchApiService.fetchDataFromApi(TOKEN, requestData));

    // then - verify the output
  }
}
