##########################################################
# Adding the lambda archive to the defined bucket        #
##########################################################
resource "aws_s3_bucket_object" "rsvp_lambda_package" {
  bucket = data.terraform_remote_state.s3_buckets.outputs.artifactory_s3_name
  key    = var.rsvp_lambda_bucket_key
  source = "${path.module}/../../rsvp-processor-lambda/target/rsvp-processor-lambda.zip"
  etag   = filemd5("${path.module}/../../rsvp-processor-lambda/target/rsvp-processor-lambda.zip")
}

resource "aws_lambda_function" "rsvp_lambda_processor" {
  depends_on = [aws_iam_role.rsvp_lambda_role, aws_iam_policy.rsvp_lambda_policy]

  description = "Lambda function to process RSVP event"

  function_name = var.rsvp_lambda
  handler       = var.rsvp_lambda_handler

  s3_bucket = aws_s3_bucket_object.rsvp_lambda_package.bucket
  s3_key    = aws_s3_bucket_object.rsvp_lambda_package.key

  source_code_hash = filebase64sha256("${path.module}/../../rsvp-processor-lambda/target/rsvp-processor-lambda.zip")
  role             = aws_iam_role.rsvp_lambda_role.arn

  memory_size = var.rsvp_lambda_memory
  timeout     = var.rsvp_lambda_timeout
  runtime     = "java8"

  environment {
    variables = {
      isRunningInLambda = "true",
      environment       = var.environment
    }
  }

  tags = merge(local.common_tags, map("Name", "${var.environment}-rsvp-processor"))
}

resource "aws_lambda_event_source_mapping" "kinesis_lambda_event_mapping" {
  batch_size        = 100
  event_source_arn  = data.terraform_remote_state.lambda_fixed_resources.outputs.kinesis_arn
  function_name     = aws_lambda_function.rsvp_lambda_processor.arn
  enabled           = true
  starting_position = "TRIM_HORIZON"
}
