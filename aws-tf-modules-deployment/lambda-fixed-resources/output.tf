output "kinesis_arn" {
  value = module.lambda-fixed-resources.kinesis_arn
}

output "stream_name" {
  value = module.lambda-fixed-resources.stream_name
}

output "dynamodb_table" {
  value = module.lambda-fixed-resources.dynamodb_table
}

output "dynamodb_arn" {
  value = module.lambda-fixed-resources.dynamodb_arn
}
