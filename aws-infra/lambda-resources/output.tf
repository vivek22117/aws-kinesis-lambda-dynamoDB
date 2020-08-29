output "deploy_bucket" {
  value = data.terraform_remote_state.backend.outputs.artifactory_bucket_name
}

output "lambda_arn" {
  value = aws_lambda_function.rsvp_lambda_processor.arn
}