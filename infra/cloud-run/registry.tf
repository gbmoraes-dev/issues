resource "google_project_service" "artifactregistry" {
  service            = "artifactregistry.googleapis.com"
  disable_on_destroy = false
}

resource "google_artifact_registry_repository" "todo" {
  location      = var.region
  repository_id = "todo"
  format        = "DOCKER"
  mode          = "STANDARD_REPOSITORY"

  cleanup_policies {
    id     = "keep-last-2"
    action = "KEEP"
    most_recent_versions {
      keep_count = 2
    }
  }

  cleanup_policy_dry_run = false

  depends_on = [google_project_service.artifactregistry]
}

resource "google_artifact_registry_repository_iam_member" "github_actions_push" {
  location   = google_artifact_registry_repository.todo.location
  repository = google_artifact_registry_repository.todo.name
  role       = "roles/artifactregistry.writer"
  member     = "serviceAccount:${google_service_account.github_actions.email}"
}

resource "google_artifact_registry_repository_iam_member" "compute_pull" {
  location   = google_artifact_registry_repository.todo.location
  repository = google_artifact_registry_repository.todo.name
  role       = "roles/artifactregistry.reader"
  member     = "serviceAccount:${var.project_number}-compute@developer.gserviceaccount.com"
}
