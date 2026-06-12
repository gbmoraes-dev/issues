output "cloud_run_url" {
  description = "Cloud Run service URL"
  value       = google_cloud_run_v2_service.todo.uri
}

output "service_account_email" {
  description = "GitHub Actions service account email"
  value       = google_service_account.github_actions.email
}

output "service_account_key" {
  description = "GitHub Actions service account key (base64)"
  value       = google_service_account_key.github_actions_key.private_key
  sensitive   = true
}
