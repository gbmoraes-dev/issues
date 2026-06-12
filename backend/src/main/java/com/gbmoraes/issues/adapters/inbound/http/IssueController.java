package com.gbmoraes.issues.adapters.inbound.http;

import com.gbmoraes.issues.adapters.inbound.http.dto.issue.CreateIssueRequest;
import com.gbmoraes.issues.adapters.inbound.http.dto.issue.IssueResponse;
import com.gbmoraes.issues.adapters.inbound.http.dto.issue.ListIssueResponse;
import com.gbmoraes.issues.adapters.inbound.http.dto.issue.UpdateIssueRequest;
import com.gbmoraes.issues.application.issue.*;
import com.gbmoraes.issues.application.issue.dto.ListIssueInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issues")
@Tag(name = "Issues", description = "Create, list, update and delete issues for the authenticated user")
@SecurityRequirement(name = "bearer-token")
public class IssueController {

  private final CreateIssueUseCase createIssueUseCase;
  private final GetIssueUseCase getIssueUseCase;
  private final ListIssuesUseCase listIssuesUseCase;
  private final UpdateIssueUseCase updateIssueUseCase;
  private final CompleteIssueUseCase completeIssueUseCase;
  private final DeleteIssueUseCase deleteIssueUseCase;

  public IssueController(
    CreateIssueUseCase createIssueUseCase,
    GetIssueUseCase getIssueUseCase,
    ListIssuesUseCase listIssuesUseCase,
    UpdateIssueUseCase updateIssueUseCase,
    CompleteIssueUseCase completeIssueUseCase,
    DeleteIssueUseCase deleteIssueUseCase
  ) {
    this.createIssueUseCase = createIssueUseCase;
    this.getIssueUseCase = getIssueUseCase;
    this.listIssuesUseCase = listIssuesUseCase;
    this.updateIssueUseCase = updateIssueUseCase;
    this.completeIssueUseCase = completeIssueUseCase;
    this.deleteIssueUseCase = deleteIssueUseCase;
  }

  @Operation(summary = "Create an issue")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Issue created successfully"),
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
    @ApiResponse(responseCode = "422", description = "Validation error")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public IssueResponse create(
    @Valid @RequestBody CreateIssueRequest request,
    @AuthenticationPrincipal UserDetails user
  ) {
    return IssueResponse.fromOutput(
      createIssueUseCase.execute(request.toInput(user.getUsername()))
    );
  }

  @Operation(summary = "List issues")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Paginated list of issues"),
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token")
  })
  @GetMapping
  public ListIssueResponse list(
    @AuthenticationPrincipal UserDetails user,
    @RequestParam(required = false) Boolean completed,
    @RequestParam(required = false) String cursor,
    @RequestParam(defaultValue = "20") int limit
  ) {
    return ListIssueResponse.fromOutput(
      listIssuesUseCase.execute(
        new ListIssueInput(user.getUsername(), completed, cursor, limit)
      )
    );
  }

  @Operation(summary = "Get an issue by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Issue found"),
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
    @ApiResponse(responseCode = "403", description = "Issue belongs to another user"),
    @ApiResponse(responseCode = "404", description = "Issue not found")
  })
  @GetMapping("/{id}")
  public IssueResponse get(
    @PathVariable String id,
    @AuthenticationPrincipal UserDetails user
  ) {
    return IssueResponse.fromOutput(
      getIssueUseCase.execute(id, user.getUsername())
    );
  }

  @Operation(summary = "Update an issue")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Issue updated successfully"),
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
    @ApiResponse(responseCode = "403", description = "Issue belongs to another user"),
    @ApiResponse(responseCode = "404", description = "Issue not found"),
    @ApiResponse(responseCode = "422", description = "Validation error")
  })
  @PatchMapping("/{id}")
  public IssueResponse update(
    @PathVariable String id,
    @Valid @RequestBody UpdateIssueRequest request,
    @AuthenticationPrincipal UserDetails user
  ) {
    return IssueResponse.fromOutput(
      updateIssueUseCase.execute(request.toInput(id, user.getUsername()))
    );
  }

  @Operation(summary = "Mark an issue as completed")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Issue marked as completed"),
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
    @ApiResponse(responseCode = "403", description = "Issue belongs to another user"),
    @ApiResponse(responseCode = "404", description = "Issue not found"),
    @ApiResponse(responseCode = "409", description = "Issue is already completed")
  })
  @PatchMapping("/{id}/complete")
  public IssueResponse complete(
    @PathVariable String id,
    @AuthenticationPrincipal UserDetails user
  ) {
    return IssueResponse.fromOutput(
      completeIssueUseCase.execute(id, user.getUsername())
    );
  }

  @Operation(summary = "Delete an issue")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Issue deleted successfully"),
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
    @ApiResponse(responseCode = "403", description = "Issue belongs to another user"),
    @ApiResponse(responseCode = "404", description = "Issue not found")
  })
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
    @PathVariable String id,
    @AuthenticationPrincipal UserDetails user
  ) {
    deleteIssueUseCase.execute(id, user.getUsername());
  }
}
