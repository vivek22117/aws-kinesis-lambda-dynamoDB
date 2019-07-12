provider "aws" {
  region  = var.default_region                                // Interpolation Syntax
  profile = var.profile

  assume_role {
    role_arn = data.terraform_remote_state.jenkins.jenkins_role
    session_name = var.session
    external_id = var.external_id
  }

  version = "2.17.0"                                            // AWS plugin version
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

provider "archive" {
  version = "1.2.2"
}

###########################################################
# Terraform configuration block is used to define backend #
# Interpolation sytanx is not allowed in Backend          #
###########################################################
terraform {
  required_version = ">= 0.12"                                       // Terraform version

  backend "s3" {
    profile        = "doubledigit"
    bucket         = "teamconcept-tfstate-dev-us-east-1"
    dynamodb_table = "teamconcept-tfstate-dev-us-east-1"
    key            = "state/dev/lambda/rsvp-lambda-kinesis-db/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = "true"
  }
}
