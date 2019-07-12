package com.ddsolutions.stream.utility;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AWSUtil {
    private static final Logger LOGGER = LogManager.getLogger(AWSUtil.class);

    private static AWSCredentialsProvider awsCredentialsProvider;

    public static AmazonSQS getSQSClient() {
        try {
            AWSCredentialsProvider awsCredentials = getAWSCredentials();
            return AmazonSQSClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred.." + ex.getMessage());
            throw ex;
        }
    }

    public static AmazonDynamoDB getDynamoDBClient() {
        try {
            AWSCredentialsProvider awsCredentials = getAWSCredentials();
            return AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred.." + ex.getMessage());
            throw ex;
        }
    }

    private static AWSCredentialsProvider getAWSCredentials() {
        if (awsCredentialsProvider == null) {
            String caller = PropertyLoader.getPropValues("caller");
            if (caller.equals("lambda")) {
                awsCredentialsProvider = new EnvironmentVariableCredentialsProvider();
            } else if (caller.equals("ec2")) {
                awsCredentialsProvider = new InstanceProfileCredentialsProvider(true);
            } else {
                awsCredentialsProvider = new ProfileCredentialsProvider("doubledigit");
            }
        }
        return awsCredentialsProvider;
    }

}
