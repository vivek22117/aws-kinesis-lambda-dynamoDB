output "kinesis_arn" {
  value = aws_kinesis_stream.rsvp_record_stream.arn
}

output "stream_name" {
  value = aws_kinesis_stream.rsvp_record_stream.name
}

output "dynamodb_table" {
  value = aws_dynamodb_table.rsvp_record_table.name
}

output "dynamodb_arn" {
  value = aws_dynamodb_table.rsvp_record_table.arn
}