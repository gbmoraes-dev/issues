resource "google_cloud_run_v2_service" "todo" {
  name     = "todo"
  location = var.region

  deletion_protection = false

  lifecycle {
    ignore_changes = [
      template,
      client,
      client_version,
    ]
  }

  template {
    containers {
      image = var.image

      resources {
        limits = {
          cpu    = "1"
          memory = "512Mi"
        }
      }

      env {
        name = "SPRING_DATASOURCE_URL"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.database_url.secret_id
            version = "latest"
          }
        }
      }

      env {
        name = "SPRING_DATASOURCE_USERNAME"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.database_user.secret_id
            version = "latest"
          }
        }
      }

      env {
        name = "SPRING_DATASOURCE_PASSWORD"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.database_password.secret_id
            version = "latest"
          }
        }
      }

      env {
        name = "JWT_SECRET"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.jwt_secret.secret_id
            version = "latest"
          }
        }
      }

      env {
        name  = "JWT_EXPIRATION_MS"
        value = var.jwt_expiration_ms
      }
    }

    scaling {
      min_instance_count = 0
      max_instance_count = 1
    }
  }

  depends_on = [
    google_project_service.run,
    google_secret_manager_secret_version.database_url,
    google_secret_manager_secret_version.database_user,
    google_secret_manager_secret_version.database_password,
    google_secret_manager_secret_version.jwt_secret,
    google_project_iam_member.compute_secret_accessor,
  ]
}

resource "google_cloud_run_v2_service_iam_member" "public" {
  project  = google_cloud_run_v2_service.todo.project
  location = google_cloud_run_v2_service.todo.location
  name     = google_cloud_run_v2_service.todo.name
  role     = "roles/run.invoker"
  member   = "allUsers"
}

