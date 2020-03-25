## RSVP Records processor & persister using AWS Lambda Serverless Model

## Table of contents
* [General info](#general-info)
* [Technologies & AWS Services](#technologies)
* [Flow-Diagram](#flow-diagram)


## General info
This project is deployed on AWS Lambda which is integrated with Kinesis Data Stream.
Data on stream will trigger the AWS Lambda which in turn process the RSVP records and store
them in DynamoDB table.


## Technologies & AWS Services
Project is created with:
* Java8
* Terraform for Iaas (Infrastructure as a Code)
* Jenkins for Automation of Infra and Code Deployment
* AWS Lambda
* AWS Kinesis Data Stream
* AWS DynamoDB 
* AWS CloudWatch