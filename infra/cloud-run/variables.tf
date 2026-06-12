variable "project_id" {
  description = "GCP Project ID"
  type        = string
}

variable "project_number" {
  description = "GCP Project Number"
  type        = string
}

variable "region" {
  description = "GCP Region"
  type        = string
  default     = "us-central1"
}

variable "domain" {
  description = "Custom domain for the API"
  type        = string
}

variable "image" {
  description = "Docker image URL"
  type        = string
  default     = "gcr.io/cloudrun/hello"
}

variable "database_url" {
  description = "PostgreSQL connection URL"
  type        = string
  sensitive   = true
}

variable "database_user" {
  description = "PostgreSQL user"
  type        = string
  sensitive   = true
}

variable "database_password" {
  description = "PostgreSQL password"
  type        = string
  sensitive   = true
}

variable "jwt_secret" {
  description = "JWT secret key"
  type        = string
  sensitive   = true
}

variable "jwt_expiration_ms" {
  description = "JWT expiration in milliseconds"
  type        = string
  default     = "86400000"
}
