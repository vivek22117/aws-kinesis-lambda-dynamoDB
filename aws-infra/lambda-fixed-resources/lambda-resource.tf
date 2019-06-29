# adding the lambda archive to the defined bucket
resource "aws_s3_bucket_object" "rsvp_lambda_package" {
  bucket                 = "${data.terraform_remote_state.backend.deploy_bucket_name}"
  key                    = "${var.rsvp_lambda_bucket_key}"
  source                 = "${path.module}/kinesis-stream-processing/target/rsvp-record-processing-1.0.0-lambda.zip"
  server_side_encryption = "AES256"
}


resource "aws_lambda_function" "rsvp_lambda_processor" {
  description = "Lambda function to process RSVP event"

  function_name = "${var.rsvp_lambda}"
  handler = "${var.rsvp_lambda_handler}"

  s3_bucket = "${aws_s3_bucket_object.rsvp_lambda_package.bucket}"
  s3_key    = "${aws_s3_bucket_object.rsvp_lambda_package.key}"

  role = "${aws_iam_role.rsvp_lambda_role.arn}"

  memory_size = "${var.rsvp_lambda_memory}"
  timeout = "${var.rsvp_lambda_timeout}"
  runtime = "python3.7"

  tags = "${merge(local.common_tags, map("Name", "${var.environment}-rsvp-processor"))}"

}