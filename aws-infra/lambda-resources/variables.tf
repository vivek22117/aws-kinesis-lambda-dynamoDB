#####===================Global Variables======================#####
variable "environment" {
  type        = string
  description = "Environment to be configured 'dev', 'qa', 'prod'"
}

#####=============================Applicaiton Variables=================#####
variable "rsvp_lambda" {
  type        = string
  description = "RSVP lambda function name"
}

variable "rsvp_lambda_handler" {
  type        = string
  description = "RSVP lambda handler name"
}

variable "rsvp_lambda_memory" {
  type        = string
  description = "RSVP lambda memory size"
}

variable "rsvp_lambda_timeout" {
  type        = string
  description = "RSVP lambda time out"
}

#####==========================Default Variables================#####
variable "default_region" {
  type    = string
  default = "us-east-1"
}

variable "s3_bucket_prefix" {
  type    = string
  default = "doubledigit-tfstate"
}

variable "rsvp_lambda_bucket_key" {
  type        = string
  description = "rsvp lambda jar s3 key"
  default     = "lambda/rsvp-lambda/rsvp_lambda_processor.zip"
}

#####===============Local variables==================#####
locals {
  common_tags = {
    owner       = "Vivek"
    team        = "DoubleDigitTeam"
    environment = var.environment
  }
}
