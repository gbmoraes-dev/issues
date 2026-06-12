package com.gbmoraes.issues.integration.issue;

import com.gbmoraes.issues.adapters.inbound.http.dto.issue.CreateIssueRequest;
import com.gbmoraes.issues.adapters.inbound.http.dto.issue.IssueResponse;
import com.gbmoraes.issues.adapters.inbound.http.dto.issue.ListIssueResponse;
import com.gbmoraes.issues.adapters.inbound.http.dto.issue.UpdateIssueRequest;
import com.gbmoraes.issues.config.IntegrationTestBase;
import com.gbmoraes.issues.integration.auth.helper.AuthHelper;
import com.gbmoraes.issues.integration.issue.helper.IssueHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

class IssueControllerIntegrationTest extends IntegrationTestBase {

  private AuthHelper authHelper;
  private IssueHelper issueHelper;

  @BeforeEach
  void setup() {
    authHelper = new AuthHelper(restTemplate);
    issueHelper = new IssueHelper(restTemplate);
  }

  // ── create ───────────────────────────────────────────────────────────────

  @Test
  void postIssuesShouldReturn201AndIssue() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);

    var response = restTemplate.exchange(
      "/issues",
      HttpMethod.POST,
      new HttpEntity<>(
        new CreateIssueRequest("Buy milk", "From the store"),
        headers
      ),
      IssueResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.CREATED
    );
    Assertions.assertThat(response.getBody().title()).isEqualTo("Buy milk");
    Assertions.assertThat(response.getBody().completed()).isFalse();
  }

  @Test
  void postIssuesShouldReturn401WithoutToken() {
    var response = restTemplate.postForEntity(
      "/issues",
      new CreateIssueRequest("Buy milk", null),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.UNAUTHORIZED
    );
  }

  @Test
  void postIssuesShouldReturn422WhenTitleIsBlank() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);

    var response = restTemplate.exchange(
      "/issues",
      HttpMethod.POST,
      new HttpEntity<>(new CreateIssueRequest("", null), headers),
      String.class
    );

    Assertions.assertThat(response.getStatusCode().value()).isEqualTo(
      HttpStatus.UNPROCESSABLE_ENTITY.value()
    );
  }

  // ── list ─────────────────────────────────────────────────────────────────

  @Test
  void getIssuesShouldReturnPaginatedIssues() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);

    issueHelper.createIssue(headers, "Issue 1");
    issueHelper.createIssue(headers, "Issue 2");

    var response = restTemplate.exchange(
      "/issues",
      HttpMethod.GET,
      new HttpEntity<>(null, headers),
      ListIssueResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody().items()).hasSize(2);
    Assertions.assertThat(response.getBody().hasMore()).isFalse();
  }

  @Test
  void getIssuesShouldFilterByCompleted() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);

    var issue = issueHelper.createIssue(headers);
    issueHelper.completeIssue(headers, issue.id());
    issueHelper.createIssue(headers, "Incomplete issue");

    var response = restTemplate.exchange(
      "/issues?completed=true",
      HttpMethod.GET,
      new HttpEntity<>(null, headers),
      ListIssueResponse.class
    );

    Assertions.assertThat(response.getBody().items()).hasSize(1);
    Assertions.assertThat(
      response.getBody().items().get(0).completed()
    ).isTrue();
  }

  @Test
  void getIssuesShouldNotReturnIssuesFromOtherUsers() {
    var token1 = authHelper.signUp("user1@test.com");
    var headers1 = authHelper.authHeaders(token1);

    var token2 = authHelper.signUp("user2@test.com");
    var headers2 = authHelper.authHeaders(token2);

    issueHelper.createIssue(headers1);

    var response = restTemplate.exchange(
      "/issues",
      HttpMethod.GET,
      new HttpEntity<>(null, headers2),
      ListIssueResponse.class
    );

    Assertions.assertThat(response.getBody().items()).isEmpty();
  }

  @Test
  void getIssuesShouldPaginateCorrectly() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);

    for (int i = 0; i < 5; i++) {
      issueHelper.createIssue(headers, "Issue " + i);
    }

    var firstPage = restTemplate
      .exchange(
        "/issues?limit=3",
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        ListIssueResponse.class
      )
      .getBody();

    Assertions.assertThat(firstPage.items()).hasSize(3);
    Assertions.assertThat(firstPage.hasMore()).isTrue();
    Assertions.assertThat(firstPage.nextCursor()).isNotNull();

    var secondPage = restTemplate
      .exchange(
        "/issues?limit=3&cursor=" + firstPage.nextCursor(),
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        ListIssueResponse.class
      )
      .getBody();

    Assertions.assertThat(secondPage.items()).hasSize(2);
    Assertions.assertThat(secondPage.hasMore()).isFalse();
  }

  // ── get ──────────────────────────────────────────────────────────────────

  @Test
  void getIssueByIdShouldReturnIssue() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);
    var issue = issueHelper.createIssue(headers);

    var response = restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.GET,
      new HttpEntity<>(null, headers),
      IssueResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody().id()).isEqualTo(issue.id());
  }

  @Test
  void getIssueByIdShouldReturn404WhenNotFound() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);

    var response = restTemplate.exchange(
      "/issues/non-existent-id",
      HttpMethod.GET,
      new HttpEntity<>(null, headers),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.NOT_FOUND
    );
  }

  @Test
  void getIssueByIdShouldReturn403WhenIssueBelongsToAnotherUser() {
    var token1 = authHelper.signUp("user1@test.com");
    var headers1 = authHelper.authHeaders(token1);

    var token2 = authHelper.signUp("user2@test.com");
    var headers2 = authHelper.authHeaders(token2);

    var issue = issueHelper.createIssue(headers1);

    var response = restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.GET,
      new HttpEntity<>(null, headers2),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.FORBIDDEN
    );
  }

  // ── update ───────────────────────────────────────────────────────────────

  @Test
  void patchIssueByIdShouldUpdateIssue() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);
    var issue = issueHelper.createIssue(headers);

    var response = restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.PATCH,
      new HttpEntity<>(new UpdateIssueRequest("New title", null), headers),
      IssueResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody().title()).isEqualTo("New title");
    Assertions.assertThat(response.getBody().description()).isEqualTo(
      issue.description()
    );
  }

  @Test
  void patchIssueByIdShouldReturn403WhenIssueBelongsToAnotherUser() {
    var token1 = authHelper.signUp("user1@test.com");
    var headers1 = authHelper.authHeaders(token1);

    var token2 = authHelper.signUp("user2@test.com");
    var headers2 = authHelper.authHeaders(token2);

    var issue = issueHelper.createIssue(headers1);

    var response = restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.PATCH,
      new HttpEntity<>(new UpdateIssueRequest("New title", null), headers2),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.FORBIDDEN
    );
  }

  // ── complete ─────────────────────────────────────────────────────────────

  @Test
  void patchIssueCompleteShouldCompleteIssue() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);
    var issue = issueHelper.createIssue(headers);

    var response = restTemplate.exchange(
      "/issues/" + issue.id() + "/complete",
      HttpMethod.PATCH,
      new HttpEntity<>(null, headers),
      IssueResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody().completed()).isTrue();
  }

  @Test
  void patchIssueCompleteShouldToggleBackToOpen() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);
    var issue = issueHelper.createIssue(headers);

    issueHelper.completeIssue(headers, issue.id());

    var response = restTemplate.exchange(
      "/issues/" + issue.id() + "/complete",
      HttpMethod.PATCH,
      new HttpEntity<>(null, headers),
      IssueResponse.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody().completed()).isFalse();
  }

  // ── delete ───────────────────────────────────────────────────────────────

  @Test
  void deleteIssueShouldDeleteIssue() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);
    var issue = issueHelper.createIssue(headers);

    var response = restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.DELETE,
      new HttpEntity<>(null, headers),
      Void.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.NO_CONTENT
    );
  }

  @Test
  void deleteIssueShouldReturn404AfterDeletion() {
    var token = authHelper.signUp();
    var headers = authHelper.authHeaders(token);
    var issue = issueHelper.createIssue(headers);

    restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.DELETE,
      new HttpEntity<>(null, headers),
      Void.class
    );

    var response = restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.GET,
      new HttpEntity<>(null, headers),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.NOT_FOUND
    );
  }

  @Test
  void deleteIssueShouldReturn403WhenIssueBelongsToAnotherUser() {
    var token1 = authHelper.signUp("user1@test.com");
    var headers1 = authHelper.authHeaders(token1);

    var token2 = authHelper.signUp("user2@test.com");
    var headers2 = authHelper.authHeaders(token2);

    var issue = issueHelper.createIssue(headers1);

    var response = restTemplate.exchange(
      "/issues/" + issue.id(),
      HttpMethod.DELETE,
      new HttpEntity<>(null, headers2),
      String.class
    );

    Assertions.assertThat(response.getStatusCode()).isEqualTo(
      HttpStatus.FORBIDDEN
    );
  }
}
