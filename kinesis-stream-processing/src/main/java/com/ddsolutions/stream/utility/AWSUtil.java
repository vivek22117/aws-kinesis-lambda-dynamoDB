package com.ddsolutions.stream.utility;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AWSUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AWSUtil.class);

    private static AWSCredentialsProvider awsCredentialsProvider;

    public AmazonSQS getSQSClient() {
        try {
            AWSCredentialsProvider awsCredentials = getAWSCredentials();
            return AmazonSQSClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occured.." + ex.getMessage());
            throw ex;
        }
    }


    private AWSCredentialsProvider getAWSCredentials() {
        if (awsCredentialsProvider == null) {
            boolean caller = Boolean.parseBoolean(PropertyLoader.getPropValues("caller"));
            if (caller) {
                awsCredentialsProvider = new InstanceProfileCredentialsProvider(true);
            } else {
                awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();
            }
        }
        return awsCredentialsProvider;
    }

}
