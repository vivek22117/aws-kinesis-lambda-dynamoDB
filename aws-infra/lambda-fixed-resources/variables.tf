//Global Variables
variable "profile" {
  type = "string"
  description = "AWS Profile name for credentials"
}

variable "environment" {
  type = "string"
  description = "AWS Profile name for credentials"
}

variable "stream_name" {
  type = "string"
  description = "Kinesis stream name"
}

variable "shard_count" {
  type = "string"
  description = "Count of shards in kinesis stream"
}

variable "stream_retention" {
  type = "string"
  description = "Retention period for kinesis stream"
}

variable "db_table_name" {
  type = "string"
  description = "DynamoDB table"
}

variable "hash_key" {
  type = "string"
  description = "DynamoDB table hash key"
}

variable "range_key" {
  type = "string"
  description = "DynamoDB table range key"
}

variable "rsvp_lambda" {
  type = "string"
  description = "RSVP lambda function name"
}

variable "rsvp_lambda_handler" {
  type = "string"
  description = "RSVP lambda handler name"
}

variable "rsvp_lambda_memory" {
  type = "string"
  description = "RSVP lambda memory size"
}

variable "rsvp_lambda_timeout" {
  type = "string"
  description = "RSVP lambda time out"
}


//Default Variables
variable "default_region" {
  type = "string"
  default = "us-east-1"
}

variable "rsvp_lambda_bucket_key" {
  type = "string"
  description = "rsvp lambda jar s3 key"
  default = "rsvp-lambda/rsvp_lambda_processor.zip"
}


//DYNAMODB TABLE
variable "s3_bucket_prefix" {
  type    = "string"
  default = "teamconcept-tfstate"
}

variable "billing_mode" {
  type = "string"
  default = "PROVISIONED"
  description = "DynamoDB Billing mode. Can be PROVISIONED or PAY_PER_REQUEST"
}

variable "enable_streams" {
  type = "string"
  default = "false"
  description = "Enable DynamoDB streams"
}

variable "stream_view_type" {
  type = "string"
  default = ""
  description = "When an item in the table is modified, what information is written to the stream KEYS_ONLY, NEW_IMAGE, OLD_IMAGE, NEW_AND_OLD_IMAGES."
}

variable "autoscale_min_read_capacity" {
  default = 2
  description = "DynamoDB autoscaling min read capacity"
}

variable "autoscale_max_read_capacity" {
  default = 10
  description = "DynamoDB autoscaling max read capacity"
}

variable "autoscale_min_write_capacity" {
  default = 2
  description = "DynamoDB autoscaling min write capacity"
}

variable "autoscale_max_write_capacity" {
  default = 10
  description = "DynamoDB autoscaling max write capacity"
}

variable "enable_encryption" {
  type = "string"
  default = "true"
  description = "Enable DynamoDB server-side encryption"
}

variable "enable_point_in_time_recovery" {
  type = "string"
  default = "false"
  description = "Enable DynamoDB point in time recovery"
}


//Local variables
locals {
  common_tags = {
    owner = "Vivek"
    team = "TeamConcept"
    environment = "${var.environment}"
  }
}
