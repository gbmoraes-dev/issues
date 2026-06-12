terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 6.0"
    }
  }

  cloud {
    organization = "gbmoraes"

    workspaces {
      name = "issues"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
}
