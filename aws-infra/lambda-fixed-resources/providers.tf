provider "aws" {
  region  = var.default_region     // Interpolation Syntax
  profile = var.profile

  version = "2.17.0"                      // AWS plugin version
}

provider "template" {
  version = "2.1.2"
}

provider "null" {
  version = "2.1.2"
}

provider "random" {
  version = "2.1.2"
}
######################################################
# Terraform configuration block is used to define backend
# Interpolation sytanx is not allowed in Backend
######################################################
terraform {
  required_version = ">= 0.11.13"              // Terraform version

  backend "s3" {
    profile        = "doubledigit"
    bucket         = "teamconcept-tfstate-dev-us-east-1"
    dynamodb_table = "teamconcept-tfstate-dev-us-east-1"
    key            = "state/dev/lambda/lm-kinesis-db/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = "true"
  }
}
