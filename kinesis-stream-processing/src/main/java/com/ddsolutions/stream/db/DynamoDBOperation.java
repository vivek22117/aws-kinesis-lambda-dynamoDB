package com.ddsolutions.stream.db;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import com.ddsolutions.stream.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class DynamoDBOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBOperation.class);

    private static final String RSVP_EVENT_INDEX = "event_id";
    private static final String RSVP_VENUE_INDEX = "venue_id";
    private static final String EQUALS_OPT = " = ";
    private static final String RSVP_RECORD = "rsvp_record";
    private static final String RSVP_ID = "rsvp_Id";

    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBOperation() {
        this.dynamoDBMapper = new DynamoDBMapper(createClient(),
                new DynamoDBMapperConfig.Builder()
                        .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.APPEND_SET).build());
    }

    public void save(LatestRSVPRecord recordObject) {
        dynamoDBMapper.save(recordObject);
        LOGGER.debug("Record persisted successfully in DynamoDB");
    }

    private final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .withCredentials(new EnvironmentVariableCredentialsProvider());

    private AmazonDynamoDB createClient() {
        return builder.build();
    }
}
