default_region = "us-east-1"

db_table_name = "rsvp-record-processor-table"
billing_mode =  "PROVISIONED"
hash_key      = "rsvp_id"
range_key     = "rsvp_makeTime"

stream_name      = "rsvp-record-processor-stream"
shard_count      = "2"
stream_retention = "24"

enable_streams = false
enable_encryption = false
enable_point_in_time_recovery = false

stream_view_type = ""

autoscale_min_read_capacity = 2
autoscale_max_read_capacity = 5
autoscale_min_write_capacity = 2
autoscale_max_write_capacity = 5
