module "lambda-processor" {
  source = "../../aws-infra-modules/module.lambda-resources"

  environment    = var.environment
  default_region = var.default_region

  rsvp_lambda         = var.rsvp_lambda
  rsvp_lambda_handler = var.rsvp_lambda_handler
  rsvp_lambda_memory  = var.rsvp_lambda_memory
  rsvp_lambda_timeout = var.rsvp_lambda_timeout
}
