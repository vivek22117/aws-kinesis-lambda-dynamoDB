output "deploy_bucket" {
  value = data.terraform_remote_state.backend.outputs.deploy_bucket_name
}

output "kinesis_arn" {
  value = aws_kinesis_stream.rsvp_record_stream.arn
}

output "stream_name" {
  value = aws_kinesis_stream.rsvp_record_stream.name
}