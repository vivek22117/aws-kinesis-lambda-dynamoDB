profile = "admin"

stream_name      = "rsvp-record-processor-stream"
shard_count      = "2"
stream_retention = "24"

db_table_name = "rsvp-record-processor-table"
range_key     = "rsvp_makeTime"
hash_key      = "rsvp_id"

rsvp_lambda         = "rsvp-lambda-processor"
rsvp_lambda_handler = "com.ddsolutions.stream.lambda.KinesisStreamProcessor::processLatestReportedEvent"
rsvp_lambda_memory  = 384
rsvp_lambda_timeout = 120