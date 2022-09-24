default_region = "us-east-1"

rsvp_lambda         = "rsvp-lambda-processor"
rsvp_lambda_handler = "com.ddsolutions.stream.lambda.KinesisStreamProcessor::processLatestReportedEvent"
rsvp_lambda_memory  = 384
rsvp_lambda_timeout = 90
rsvp_lambda_bucket_key = "lambda/rsvp-lambda-processor/rsvp_lambda_processor.zip"
