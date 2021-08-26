###################################################
#       Configuration to fetch remote state       #
#       of artifactory bucket for deployment      #
###################################################
data "terraform_remote_state" "s3_buckets" {
  backend = "s3"

  config = {
    bucket  = "${var.s3_bucket_prefix}-${var.environment}-${var.default_region}"
    key     = "state/${var.environment}/s3-buckets/terraform.tfstate"
    region  = var.default_region
  }
}

data "terraform_remote_state" "lambda_fixed_resources" {
  backend = "s3"

  config = {
    bucket  = "${var.s3_bucket_prefix}-${var.environment}-${var.default_region}"
    key     = "state/${var.environment}/lambda/rsvp-lambda-fixed-resources/terraform.tfstate"
    region  = var.default_region
  }
}
