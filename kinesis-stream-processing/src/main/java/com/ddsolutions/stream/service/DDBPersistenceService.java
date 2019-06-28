package com.ddsolutions.stream.service;

import com.ddsolutions.stream.db.DynamoDBOperation;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import com.ddsolutions.stream.utility.JsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashSet;

import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.SECONDS;


public class DDBPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DDBPersistenceService.class);

    private DynamoDBOperation dynamoDBOperation;
    private JsonUtility jsonUtility;


    public DDBPersistenceService() {
        this(new DynamoDBOperation(), new JsonUtility());
    }

    private DDBPersistenceService(DynamoDBOperation dynamoDBOperation, JsonUtility jsonUtility) {
        this.dynamoDBOperation = dynamoDBOperation;
        this.jsonUtility = jsonUtility;
    }

    public void processRecord(RSVPEventRecord rsvpEventRecord) throws JsonProcessingException {
        HashSet<LatestRSVPRecord> reportedRecords = new HashSet<>();
        Instant rsvpTime = Instant.ofEpochMilli(rsvpEventRecord.getMtime());

        reportedRecords.add(createDDBRecord(rsvpEventRecord, rsvpTime));

        reportedRecords.forEach(reportedRecord -> dynamoDBOperation.save(reportedRecord));
    }

    private LatestRSVPRecord createDDBRecord(RSVPEventRecord rsvpEventRecord, Instant rsvpTime) throws JsonProcessingException {
        LatestRSVPRecord latestRSVPRecord = new LatestRSVPRecord();
        latestRSVPRecord.setRsvpId(valueOf(rsvpEventRecord.getRsvp_id()));
        latestRSVPRecord.setRsvpMakeTime(rsvpTime.toString());
        latestRSVPRecord.setCreatedDate(Instant.now().truncatedTo(SECONDS).toString());
        latestRSVPRecord.setRsvpEventId(createRsvpEventId(rsvpEventRecord));
        latestRSVPRecord.setRsvpVenueId(createRsvpVenueId(rsvpEventRecord));

        String rsvpRecord = jsonUtility.convertToJson(rsvpEventRecord);
        latestRSVPRecord.setRsvpEventRecord(rsvpRecord);
        return latestRSVPRecord;
    }

    private String createRsvpVenueId(RSVPEventRecord rsvpEventRecord) {
        return valueOf(rsvpEventRecord.getRsvp_id()).concat("-").concat(valueOf(rsvpEventRecord.getVenue().getVenue_id()));
    }

    private String createRsvpEventId(RSVPEventRecord rsvpEventRecord) {
        return valueOf(rsvpEventRecord.getMtime()).concat("-").concat(rsvpEventRecord.getEvent().getEvent_id());
    }
}
