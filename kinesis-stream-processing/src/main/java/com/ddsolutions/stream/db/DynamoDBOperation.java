package com.ddsolutions.stream.db;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DynamoDBOperation {

    private static final Logger LOGGER = LogManager.getLogger(DynamoDBOperation.class);

    private static final String RSVP_EVENT_INDEX = "event_id";
    private static final String RSVP_VENUE_INDEX = "venue_id";
    private static final String EQUALS_OPT = " = ";
    private static final String RSVP_RECORD = "rsvp_record";
    private static final String RSVP_ID = "rsvp_Id";

    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBOperation() {
        this.dynamoDBMapper = new DynamoDBMapper(createClient(),
                new DynamoDBMapperConfig.Builder()
                        .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE).build());
    }

    public void save(LatestRSVPRecord recordObject) {
        try {
            LOGGER.debug("Persisting started.....");
            dynamoDBMapper.save(recordObject);
            LOGGER.debug("Record persisted successfully in DynamoDB");
        } catch (Exception ex) {
            LOGGER.error("Unable to persist record");
        }
    }

    private final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .withCredentials(new EnvironmentVariableCredentialsProvider());

    private AmazonDynamoDB createClient() {
        return builder.build();
    }
}
