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
import java.util.TimeZone;

import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.SECONDS;


public class DDBPersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DDBPersistenceService.class);

    private DynamoDBProcessing dynamoDBProcessing;
    private JsonUtility jsonUtility;

    static {
        TimeZone.getTimeZone("UTC");
    }

    public DDBPersistenceService() {
        this(new DynamoDBProcessing(), new JsonUtility());
    }

    DDBPersistenceService(DynamoDBProcessing dynamoDBProcessing, JsonUtility jsonUtility) {
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

    public void fetchRSVPRecords(String rsvpId, String eventId, String date, int numberOfRecords) {
        dynamoDBProcessing.getReportedDatesByEndDate(rsvpId, eventId, numberOfRecords, date);
    }

    private LatestRSVPRecord createDDBRecord(RSVPEventRecord rsvpEventRecord, Instant rsvpTime) throws JsonProcessingException {
        LatestRSVPRecord latestRSVPRecord = new LatestRSVPRecord();

        latestRSVPRecord.setExpiry_time(Instant.now().plus(300, SECONDS).toEpochMilli() / 1000L);
//        latestRSVPRecord.setExpiry_time(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli() / 1000L);

        int randomInt = 0; //= new Random().ints(9999991, 9999994).findFirst().getAsInt();

        latestRSVPRecord.setRsvp_id(valueOf(rsvpEventRecord.getRsvp_id()));
//        latestRSVPRecord.setRsvp_id(valueOf(randomInt));
        latestRSVPRecord.setRsvp_makeTime(rsvpTime.toString());
//        latestRSVPRecord.setRsvp_makeTime("2019-07-05T14:22:56.245Z");
        latestRSVPRecord.setCreated_time(Instant.now().truncatedTo(SECONDS).toString());
        latestRSVPRecord.setRsvp_with_event_id(createRsvpEventId(rsvpEventRecord, randomInt));
        latestRSVPRecord.setRsvp_with_venue_id(createRsvpVenueId(rsvpEventRecord, randomInt));

        String rsvpRecord = jsonUtility.convertToJson(rsvpEventRecord);
        Set<String> rsvpRecordSet = new HashSet<>();
        rsvpRecordSet.add(rsvpRecord);
        latestRSVPRecord.setRsvp_record(rsvpRecordSet);

        //DynamoDB Query call for testing
//        fetchRSVPRecords(latestRSVPRecord.getRsvp_id(), latestRSVPRecord.getRsvp_with_event_id(), latestRSVPRecord.getRsvp_makeTime(), 1);
        return latestRSVPRecord;
    }

    private String createRsvpVenueId(RSVPEventRecord rsvpEventRecord, int randomInt) {
//        int randomVenueInt = new Random().ints(2999991, 2999993).findFirst().getAsInt();
        if (Objects.nonNull(rsvpEventRecord.getVenue())) {
            return valueOf(rsvpEventRecord.getRsvp_id()).concat("-").concat(valueOf(rsvpEventRecord.getVenue().getVenue_id()));
//            return valueOf(randomInt).concat("-").concat(valueOf(randomVenueInt));
        }
        return valueOf(rsvpEventRecord.getRsvp_id()).concat("-").concat("0");
//        return valueOf(randomInt).concat("-").concat("0");
    }

    private String createRsvpEventId(RSVPEventRecord rsvpEventRecord, int randomInt) {
//        int randomEventInt = new Random().ints(1999991, 1999993).findFirst().getAsInt();
        return valueOf(rsvpEventRecord.getRsvp_id()).concat("-").concat(rsvpEventRecord.getEvent().getEvent_id());
//        return valueOf(randomInt).concat("-").concat(String.valueOf(randomEventInt));
    }
}
