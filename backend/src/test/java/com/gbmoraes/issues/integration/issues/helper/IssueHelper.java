package com.gbmoraes.issues.integration.issue.helper;

import com.gbmoraes.issues.adapters.inbound.http.dto.issue.CreateIssueRequest;
import com.gbmoraes.issues.adapters.inbound.http.dto.issue.IssueResponse;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class IssueHelper {

  private final TestRestTemplate restTemplate;

  public IssueHelper(TestRestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public IssueResponse createIssue(HttpHeaders headers) {
    return createIssue(headers, "Buy milk");
  }

  public IssueResponse createIssue(HttpHeaders headers, String title) {
    return restTemplate
      .exchange(
        "/issues",
        HttpMethod.POST,
        new HttpEntity<>(
          new CreateIssueRequest(title, "From the store"),
          headers
        ),
        IssueResponse.class
      )
      .getBody();
  }

  public IssueResponse completeIssue(HttpHeaders headers, String id) {
    return restTemplate
      .exchange(
        "/issues/" + id + "/complete",
        HttpMethod.PATCH,
        new HttpEntity<>(null, headers),
        IssueResponse.class
      )
      .getBody();
  }
}
