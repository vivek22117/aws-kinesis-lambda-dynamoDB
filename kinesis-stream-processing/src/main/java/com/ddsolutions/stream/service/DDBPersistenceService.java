package com.ddsolutions.stream.service;

import com.ddsolutions.stream.db.DynamoDBProcessing;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import com.ddsolutions.stream.utility.JsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.SECONDS;


public class DDBPersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DDBPersistenceService.class);

    private DynamoDBProcessing dynamoDBProcessing;
    private JsonUtility jsonUtility;


    public DDBPersistenceService() {
        this(new DynamoDBProcessing(), new JsonUtility());
    }

    private DDBPersistenceService(DynamoDBProcessing dynamoDBProcessing, JsonUtility jsonUtility) {
        this.dynamoDBProcessing = dynamoDBProcessing;
        this.jsonUtility = jsonUtility;
    }

    public void processRecord(RSVPEventRecord rsvpEventRecord) throws JsonProcessingException {
        HashSet<LatestRSVPRecord> reportedRecords = new HashSet<>();
        Instant rsvpTime = Instant.ofEpochMilli(rsvpEventRecord.getMtime());

        reportedRecords.add(createDDBRecord(rsvpEventRecord, rsvpTime));

        reportedRecords.forEach(reportedRecord -> dynamoDBProcessing.save(reportedRecord));
        LOGGER.debug("RSVP record persistence completed!");
    }

    private LatestRSVPRecord createDDBRecord(RSVPEventRecord rsvpEventRecord, Instant rsvpTime) throws JsonProcessingException {
        LatestRSVPRecord latestRSVPRecord = new LatestRSVPRecord();

        latestRSVPRecord.setExpiryTime(String.valueOf(Instant.now().plus(900, SECONDS).toEpochMilli()));

        latestRSVPRecord.setRsvpId(valueOf(rsvpEventRecord.getRsvp_id()));
        latestRSVPRecord.setRsvpMakeTime(rsvpTime.toString());
        latestRSVPRecord.setCreatedDate(Instant.now().truncatedTo(SECONDS).toString());
        latestRSVPRecord.setRsvpEventId(createRsvpEventId(rsvpEventRecord));
        latestRSVPRecord.setRsvpVenueId(createRsvpVenueId(rsvpEventRecord));

        String rsvpRecord = jsonUtility.convertToJson(rsvpEventRecord);
        Set<String> rsvpRecordSet = new HashSet<>();
        rsvpRecordSet.add(rsvpRecord);
        latestRSVPRecord.setRsvpEventRecord(rsvpRecordSet);
        return latestRSVPRecord;
    }

    private String createRsvpVenueId(RSVPEventRecord rsvpEventRecord) {
        if (Objects.nonNull(rsvpEventRecord.getVenue())) {
            return valueOf(rsvpEventRecord.getRsvp_id()).concat("-").concat(valueOf(rsvpEventRecord.getVenue().getVenue_id()));
        }
        return valueOf(rsvpEventRecord.getRsvp_id()).concat("-").concat("0");
    }

    private String createRsvpEventId(RSVPEventRecord rsvpEventRecord) {
        return valueOf(rsvpEventRecord.getRsvp_id()).concat("-").concat(rsvpEventRecord.getEvent().getEvent_id());
    }
}
