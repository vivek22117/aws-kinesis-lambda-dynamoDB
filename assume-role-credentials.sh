#!/bin/bash

profile_name='default'
token_duration="14400"
role_arn='arn:aws:iam::AWS_ACCOUNT_ID:role/<AWS_ASSUME_ROLE_NAME>'

STS=$(aws sts assume-role --role-arn $role_arn \
      --role-session-name TF-Session --duration-seconds $token_duration \
      --query 'Credentials.[AccessKeyId,SecretAccessKey,SessionToken]' \
      --output text --profile $profile_name)

AWS_KEY=$(echo $STS | awk '{print $1}')
AWS_SECRET=$(echo $STS | awk '{print $2}')
AWS_TOKEN=$(echo $STS | awk '{print $3}')

aws configure set profile.eks-admin.aws_access_key_id $AWS_KEY
aws configure set profile.eks-admin.aws_secret_access_key $AWS_SECRET
aws configure set profile.eks-admin.aws_session_token $AWS_TOKEN
aws configure set profile.eks-admin.region us-east-1