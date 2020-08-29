package com.ddsolutions.stream.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.CollectionUtils;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import com.ddsolutions.stream.exception.ApplicationException;
import com.ddsolutions.stream.utility.AWSUtil;
import com.ddsolutions.stream.utility.JsonUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class DynamoDBProcessing {
    private static final Logger LOGGER = LogManager.getLogger(DynamoDBProcessing.class);

    private static final String EVENT_INDEX = "RSVPEventIndex";
    private static final String VENUE_INDEX = "RSVPVenueIndex";
    private static final String PROJECTION_FIELD = "rsvp_record";
    private static final String EQUALS_OPT = " = ";
    private static final String DELIMITER = "-";

    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBProcessing() {
        this.dynamoDBMapper = new DynamoDBMapper(AWSUtil.getDynamoDBClient(),
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
            LOGGER.error("Unable to persist latest rsvp record {}", recordObject.toString(), ex);
            throw new ApplicationException("Exception in dynamoDB save api call..", ex);
        }
    }

    public List<RSVPEventRecord> getReportedDatesByEventId(String rsvpId, String eventId, int numOfRecords, String endDate) {
        String itemKey = rsvpId + DELIMITER + eventId;
        String indexPartitionKey = getIndexPartitionKey(EVENT_INDEX);

        Map<String, AttributeValue> attributeMap = new HashMap<>();
        attributeMap.put(":key", new AttributeValue().withS(eventId));
        attributeMap.put(":endDate", new AttributeValue().withS(endDate));

        DynamoDBQueryExpression<LatestRSVPRecord> queryExpression = new DynamoDBQueryExpression<LatestRSVPRecord>()
                .withIndexName(EVENT_INDEX)
                .withConsistentRead(false)
                .addExpressionAttributeValuesEntry(":key", new AttributeValue().withS(itemKey))
                .withKeyConditionExpression(
                        indexPartitionKey + EQUALS_OPT + ":key" + " AND rsvp_makeTime <= :endDate")
                .withExpressionAttributeValues(attributeMap)
                .withScanIndexForward(false)
                .withProjectionExpression(PROJECTION_FIELD)
                .withLimit(numOfRecords);

        QueryResultPage<LatestRSVPRecord> query = dynamoDBMapper.queryPage(LatestRSVPRecord.class, queryExpression);
        if (CollectionUtils.isNullOrEmpty(query.getResults())) {
            return new ArrayList<>();
        }

        List<RSVPEventRecord> rsvpRecords = query.getResults().
                stream()
                .map(LatestRSVPRecord::getRsvp_record)
                .flatMap(Collection::stream)
                .map(record -> {
                    try {
                        return new JsonUtility().convertFromJson(record, RSVPEventRecord.class);
                    } catch (IOException e) {
                        LOGGER.error("Unable to parse json string");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toList());

        return rsvpRecords.stream()
                .sorted(Comparator.comparingLong(RSVPEventRecord::getMtime).reversed())
                .collect(toList());
    }

    private String getIndexPartitionKey(String rsvpEventIndex) {
        if (rsvpEventIndex.equals(EVENT_INDEX)) {
            return "rsvp_with_event_id";
        } else {
            return "rsvp_with_venue_id";
        }
    }
}
