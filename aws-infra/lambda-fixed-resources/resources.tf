resource "aws_kinesis_stream" "rsvp_record_stream" {
  name             = "${var.stream_name}-${var.environment}"
  shard_count      = var.shard_count
  retention_period = var.stream_retention

  tags = "${merge(local.common_tags, map("Name", "rsvp-stream"))}"
}


resource "aws_dynamodb_table" "rsvp_record_table" {
  name = var.db_table_name

  hash_key         = var.hash_key
  range_key        = var.range_key
  billing_mode     = var.billing_mode
  stream_enabled   = var.enable_streams
  stream_view_type = var.stream_view_type

  read_capacity  = var.autoscale_min_read_capacity
  write_capacity = var.autoscale_min_write_capacity

  server_side_encryption {
    enabled = var.enable_encryption
  }

  point_in_time_recovery {
    enabled = var.enable_point_in_time_recovery
  }

  ttl {
    attribute_name = "created_date"
    enabled        = true
  }

  attribute {
    name = "rsvp_event_id"
    type = "S"
  }

  attribute {
    name = "rsvp_venue_id"
    type = "S"
  }

  attribute {
    name = var.hash_key
    type = "S"
  }

  attribute {
    name = var.range_key
    type = "S"
  }

  global_secondary_index {
    name               = "EventIdIndex"
    hash_key           = "rsvp_event_id"
    range_key          =  var.range_key
    write_capacity     = 2
    read_capacity      = 2
    projection_type    = "KEYS_ONLY"
  }

  global_secondary_index {
    name               = "VenueIdIndex"
    hash_key           = "rsvp_venue_id"
    range_key          = var.range_key
    write_capacity     = 2
    read_capacity      = 2
    projection_type    = "KEYS_ONLY"
  }

  tags = "${merge(local.common_tags, map("Name", "rsvp-dynamoDB"))}"
}