resource "aws_iam_role" "rsvp_lambda_role" {
  depends_on = ["aws_iam_policy.rsvp_lambda_policy"]

  name = "RSVPLambdaRole"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_policy" "rsvp_lambda_policy" {
  name = "RSVPLambdaProcessorPolicy"
  description = "Policy to access DynamoDB"
  path = "/"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:DescribeTable",
        "dynamodb:Query",
        "dynamodb:Scan",
        "dynamodb:PutItem",
        "dynamodb:UpdateItem",
        "dynamodb:DeleteItem"
      ],
      "Resource": "${aws_dynamodb_table.rsvp_record_table.arn}"
    },
    {
      "Effect": "Allow",
      "Action": [
                "kinesis:DescribeStream",
                "kinesis:DescribeStreamSummary",
                "kinesis:GetRecords",
                "kinesis:GetShardIterator",
                "kinesis:ListShards",
                "kinesis:ListStreams",
                "kinesis:SubscribeToShard"
      ],
      "Resource": "${aws_kinesis_stream.rsvp_record_stream.arn}"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_dynamodb_policy_role_att" {
  policy_arn = aws_iam_policy.rsvp_lambda_policy.arn
  role       = aws_iam_role.rsvp_lambda_role.name
}