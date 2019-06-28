package com.ddsolutions.stream.db;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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
    private static final String TABLE_NAME = "RSVPEventTable";
    private static final String RSVP_EVENT_INDEX = "event_id";
    private static final String RSVP_VENUE_INDEX = "venue_id";
    private static final String EQUALS_OPT = " = ";
    private static final String RANGE_KEY = "reportedDate";
    private static final String RSVP_RECORD = "rsvp_record";
    private static final String RSVP_ID = "rsvp_Id";

    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBOperation() {
        this.dynamoDBMapper = new DynamoDBMapper(createClient());
    }

    public void save(LatestRSVPRecord recordObject) {
        dynamoDBMapper.save(recordObject);
        LOGGER.debug("Record persisted successfully in DynamoDB");
    }

    public List<String> getEventByRSVPAndEventId(int rsvpId, String eventId, int numOfRecords) throws ApplicationException {
        return getByEventOrVenueIndex(rsvpId, eventId, RSVP_EVENT_INDEX, numOfRecords);
    }

    public List<String> getEventByRSPVAndVenueId(int rsvpId, String venueId, int numOfRecords) {
        return getByEventOrVenueIndex(rsvpId, venueId, RSVP_VENUE_INDEX, numOfRecords);
    }

    private List<String> getByEventOrVenueIndex(int rsvpId, String secondaryId, String secondaryIndex, int numOfRecords) {
        DynamoDBQueryExpression<LatestRSVPRecord> queryExpression = null;

        Optional<String> searchIndex = getEventOrVenuePartitionKey(rsvpId, secondaryId, secondaryIndex);

        if (searchIndex.isPresent()) {
            queryExpression = new DynamoDBQueryExpression<LatestRSVPRecord>()
                    .withIndexName(secondaryIndex)
                    .addExpressionAttributeValuesEntry(":key", new AttributeValue().withS(searchIndex.get()))
                    .withKeyConditionExpression(secondaryIndex + EQUALS_OPT + ":key")
                    .withScanIndexForward(false)
                    .withConsistentRead(false)
                    .withProjectionExpression(RSVP_RECORD)
                    .withLimit(numOfRecords);
        }
        QueryResultPage<LatestRSVPRecord> query = dynamoDBMapper.queryPage(LatestRSVPRecord.class, queryExpression);
        if (isNull(query.getResults())) {
            return new ArrayList<>();
        }
        return query.getResults()
                .stream()
                .sorted(new Comparator<LatestRSVPRecord>() {
                    @Override
                    public int compare(LatestRSVPRecord lrsvp1, LatestRSVPRecord lrsvp2) {
                        return lrsvp1.getRsvpMakeTime().compareTo(lrsvp2.getRsvpMakeTime());
                    }
                }).map(LatestRSVPRecord::getRsvpEventRecord).collect(toList());
    }

    private Optional<String> getEventOrVenuePartitionKey(int rsvpId, String secondaryId, String secondaryIndex) {
        String partitionKey = String.valueOf(rsvpId).concat("-").concat(secondaryId);
        if (RSVP_EVENT_INDEX.equals(secondaryIndex)) {
            return Optional.of(partitionKey);
        } else if (RSVP_VENUE_INDEX.equals(secondaryIndex)) {
            return Optional.of(partitionKey);
        }
        return Optional.empty();
    }

    private Map<String, AttributeValue> getItemKey(String rsvpId) {
        Map<String, AttributeValue> itemToFetch = new HashMap<>();
        itemToFetch.put(RSVP_ID, new AttributeValue().withS(rsvpId));
        return itemToFetch;
    }

    private List<String> getLastReportedRsvp(Integer rsvpId, String startDate, String endDate,
                                             String eventId, int recordsCount) {

        Map<String, AttributeValue> expressionAttribute = new HashMap<>();
        expressionAttribute.put("rsvpId", new AttributeValue().withS(String.valueOf(rsvpId)));
        expressionAttribute.put("rsvpMakeTime", new AttributeValue().withS(endDate));
        expressionAttribute.put("createdDate", new AttributeValue().withS(endDate));
        return new ArrayList<String>();
    }

    private final AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-1")
            .withCredentials(new InstanceProfileCredentialsProvider(true));

    private AmazonDynamoDB createClient() {
        return builder.build();
    }
}
