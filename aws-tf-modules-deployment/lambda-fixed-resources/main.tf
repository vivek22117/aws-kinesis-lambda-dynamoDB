module "lambda-fixed-resources" {
  source = "../../aws-infra-modules/module.lambda-fixed-resources"

  environment    = var.environment
  default_region = var.default_region

  autoscale_max_read_capacity  = var.autoscale_max_read_capacity
  autoscale_max_write_capacity = var.autoscale_max_write_capacity
  autoscale_min_read_capacity  = var.autoscale_min_read_capacity
  autoscale_min_write_capacity = var.autoscale_min_write_capacity

  db_table_name     = var.db_table_name
  billing_mode      = var.billing_mode
  enable_encryption = var.enable_encryption
  hash_key          = var.hash_key
  range_key         = var.range_key
  shard_count       = var.shard_count

  enable_streams                = var.enable_streams
  stream_name                   = var.stream_name
  stream_retention              = var.stream_retention
  stream_view_type              = var.stream_view_type
  enable_point_in_time_recovery = var.enable_point_in_time_recovery
}
