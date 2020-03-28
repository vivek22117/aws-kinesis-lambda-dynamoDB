package com.ddsolutions.stream.service;

import com.ddsolutions.stream.db.DynamoDBProcessing;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import com.ddsolutions.stream.exception.ApplicationException;
import com.ddsolutions.stream.utility.AppUtility;
import com.ddsolutions.stream.utility.JsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.SECONDS;


public class DDBPersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DDBPersistenceService.class);

    private CloudwatchOperation cwOperation;
    private DynamoDBProcessing dynamoDBProcessing;
    private JsonUtility jsonUtility;

    static {
        TimeZone.getTimeZone("UTC");
    }

    public DDBPersistenceService() {
        this(new DynamoDBProcessing(), new JsonUtility(), new CloudwatchOperation());
    }

    DDBPersistenceService(DynamoDBProcessing dynamoDBProcessing, JsonUtility jsonUtility,
                          CloudwatchOperation cwOperation) {
        this.dynamoDBProcessing = dynamoDBProcessing;
        this.jsonUtility = jsonUtility;
        this.cwOperation = cwOperation;
    }

    public void processRecord(RSVPEventRecord rsvpEventRecord) throws JsonProcessingException {
        List<LatestRSVPRecord> reportedRecords = new ArrayList<>();
        Instant rsvpTime = Instant.ofEpochMilli(rsvpEventRecord.getMtime());

        reportedRecords.add(createDDBRecord(rsvpEventRecord, rsvpTime));

        reportedRecords.forEach(reportedRecord -> {
            try {
                dynamoDBProcessing.save(reportedRecord);

                LOGGER.debug("Last reported record persisted: \n"
                        + "rsvpEventId: " + reportedRecord.getRsvp_id() + "\n"
                        + "rsvpWithEventId: " + reportedRecord.getRsvp_with_event_id() + "\n"
                        + "rsvpWithVenueId: " + reportedRecord.getRsvp_with_venue_id() + "\n"
                        + "rsvpMakeTime: " + reportedRecord.getRsvp_makeTime() + "\n");

                cwOperation.putMetricDataWithCount("RecordsProcessed", "RSVPRecordsDetails", "RSVPRecordsCount", (double) reportedRecords.size());
            } catch (ApplicationException ex) {
                LOGGER.error(ex.getMessage());
                cwOperation.putMetricDataWithCount("RecordsFailed", "RSVPRecordsDetails", "RSVPRecordsCount", (double) reportedRecords.size());
            }
        });
    }

    public void fetchRSVPRecords(String rsvpId, String eventId, String date, int numberOfRecords) {
        dynamoDBProcessing.getReportedDatesByEventId(rsvpId, eventId, numberOfRecords, date);
    }

    private LatestRSVPRecord createDDBRecord(RSVPEventRecord rsvpEventRecord, Instant rsvpTime)
            throws JsonProcessingException {
        LatestRSVPRecord latestRSVPRecord = new LatestRSVPRecord();

        latestRSVPRecord.setExpiry_time(Instant.now().plus(2, ChronoUnit.DAYS).toEpochMilli() / 1000L);
        latestRSVPRecord.setCreated_time(Instant.now().truncatedTo(SECONDS).toString());

        latestRSVPRecord.setRsvp_with_event_id(createRsvpEventId(rsvpEventRecord));
        latestRSVPRecord.setRsvp_with_venue_id(createRsvpVenueId(rsvpEventRecord));

        latestRSVPRecord.setRsvp_id(valueOf(rsvpEventRecord.getRsvp_id()));
        latestRSVPRecord.setRsvp_makeTime(AppUtility.getTimeStampToDate(rsvpTime.toString()));

        String rsvpRecord = jsonUtility.convertToJson(rsvpEventRecord);
        Set<String> rsvpRecordSet = new HashSet<>();
        rsvpRecordSet.add(rsvpRecord);
        latestRSVPRecord.setRsvp_record(rsvpRecordSet);

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
