#####===================Global Variables======================#####
variable "environment" {
  type        = string
  description = "Environment to be configured 'dev', 'qa', 'prod'"
}


#####=============================Applicaiton Variables=================#####
variable "stream_name" {
  type        = string
  description = "Kinesis stream name"
}

variable "shard_count" {
  type        = string
  description = "Count of shards in kinesis stream"
}

variable "stream_retention" {
  type        = string
  description = "Retention period for kinesis stream"
}

variable "db_table_name" {
  type        = string
  description = "DynamoDB table"
}

variable "hash_key" {
  type        = string
  description = "DynamoDB table hash key"
}

variable "range_key" {
  type        = string
  description = "DynamoDB table range key"
}

#####=========================DynamoDB Configuration=================#####
variable "billing_mode" {
  type        = string
  description = "DynamoDB Billing mode. Can be PROVISIONED or PAY_PER_REQUEST"
}

variable "enable_streams" {
  type        = bool
  description = "Enable DynamoDB streams"
}

variable "stream_view_type" {
  type        = string
  description = "When an item in the table is modified, what information is written to the stream KEYS_ONLY, NEW_IMAGE, OLD_IMAGE, NEW_AND_OLD_IMAGES."
}

variable "autoscale_min_read_capacity" {
  type = number
  description = "DynamoDB autoscaling min read capacity"
}

variable "autoscale_max_read_capacity" {
  type = number
  description = "DynamoDB autoscaling max read capacity"
}

variable "autoscale_min_write_capacity" {
  type = number
  description = "DynamoDB autoscaling min write capacity"
}

variable "autoscale_max_write_capacity" {
  type = number
  description = "DynamoDB autoscaling max write capacity"
}

variable "enable_encryption" {
  type        = bool
  description = "Enable DynamoDB server-side encryption"
}

variable "enable_point_in_time_recovery" {
  type        = bool
  description = "Enable DynamoDB point in time recovery"
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

#####===============Local variables==================#####
locals {
  common_tags = {
    owner       = "Vivek"
    team        = "DoubleDigitTeam"
    environment = var.environment
  }
}
