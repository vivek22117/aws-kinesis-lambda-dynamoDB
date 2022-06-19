output "deploy_bucket" {
  value = data.terraform_remote_state.s3_buckets.outputs.artifactory_s3_name
}

output "lambda_arn" {
  value = aws_lambda_function.rsvp_lambda_processor.arn
}
