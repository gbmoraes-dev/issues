resource "google_service_account" "github_actions" {
  account_id   = "github-actions"
  display_name = "GitHub Actions"
}

resource "google_project_iam_member" "cloud_run_admin" {
  depends_on = [google_project_service.resourcemanager]

  project = var.project_id
  role    = "roles/run.admin"
  member  = "serviceAccount:${google_service_account.github_actions.email}"
}

resource "google_project_iam_member" "service_account_user" {
  depends_on = [google_project_service.resourcemanager]

  project = var.project_id
  role    = "roles/iam.serviceAccountUser"
  member  = "serviceAccount:${google_service_account.github_actions.email}"
}

resource "google_service_account_key" "github_actions_key" {
  depends_on = [google_project_service.resourcemanager]

  service_account_id = google_service_account.github_actions.name
}
