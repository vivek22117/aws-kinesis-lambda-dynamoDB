stream_name      = "rsvp-record-processor-stream"
shard_count      = "2"
stream_retention = "24"

db_table_name = "rsvp-record-processor-table"
range_key     = "rsvp_makeTime"
hash_key      = "rsvp_id"
billing_mode =  "PROVISIONED"
enable_streams = false
enable_encryption = false
enable_point_in_time_recovery = false
stream_view_type = ""
autoscale_min_read_capacity = 2
autoscale_max_read_capacity = 5
autoscale_min_write_capacity = 2
autoscale_max_write_capacity = 5
