package com.ddsolutions.stream.db;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.CollectionUtils;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class DynamoDBProcessing {
    private static final Logger LOGGER = LogManager.getLogger(DynamoDBProcessing.class);

    private static final String EVENT_INDEX = "RSVPEventIndex";
    private static final String VENUE_INDEX = "RSVPVenueIndex";

    private static final String PROJECTION_FIELD = "rsvp_record,event,venue";

    private static final String EQUALS_OPT = " = ";
    private static final String DELIMITER = "-";

    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBProcessing() {
        this.dynamoDBMapper = new DynamoDBMapper(createClient(),
                new DynamoDBMapperConfig.Builder()
                        .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.APPEND_SET)
                        .build());
    }

    public void save(LatestRSVPRecord recordObject) {
        try {
            LOGGER.debug("Persisting started.....");
            dynamoDBMapper.save(recordObject);
            LOGGER.debug("Record persisted successfully in DynamoDB");
        } catch (Exception ex) {
            LOGGER.error("Unable to persist latest rsvp record", ex);
        }
    }

    public List<String> getReportedDatesByEndDate(String rsvpId, String  eventId, int numOfRecords, String endDate) {
        String itemKey = rsvpId + DELIMITER + eventId;
        String indexPartitionKey = getIndexPartitionKey(EVENT_INDEX);

        Map<String, AttributeValue> attributeMap = new HashMap<>();
        attributeMap.put(":key", new AttributeValue().withS(itemKey));
        attributeMap.put(":endDate", new AttributeValue().withS(endDate));

        DynamoDBQueryExpression<LatestRSVPRecord> queryExpression = new DynamoDBQueryExpression<LatestRSVPRecord>()
                .withIndexName(EVENT_INDEX)
                .withConsistentRead(false)
                .addExpressionAttributeValuesEntry(":key", new AttributeValue().withS(itemKey))
                .withKeyConditionExpression(
                        indexPartitionKey + EQUALS_OPT + ":key" + " AND reportedDate <= :endDate")
                .withExpressionAttributeValues(attributeMap)
                .withScanIndexForward(false)
                .withProjectionExpression(PROJECTION_FIELD)
                .withLimit(numOfRecords);

        QueryResultPage<LatestRSVPRecord> query = dynamoDBMapper.queryPage(LatestRSVPRecord.class, queryExpression);
        if (CollectionUtils.isNullOrEmpty(query.getResults())) {
            return new ArrayList<>();
        }

        /*Set<String> itemResult = query.getResults().
                parallelStream().map(LatestRSVPRecord::getRsvpEventRecord).collect(toSet());*/

        return null;//itemResult.stream().sorted(comparing(String::intern).reversed()).collect(toList());
    }

    private String getIndexPartitionKey(String rsvpEventIndex) {
        if(rsvpEventIndex.equals(EVENT_INDEX)){
            return "rsvp_with_event_id";
        } else {
            return "rsvp_with_venue_id";
        }
    }

    private final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .withCredentials(new ProfileCredentialsProvider("doubledigit"));

    private AmazonDynamoDB createClient() {
        return builder.build();
    }

}
