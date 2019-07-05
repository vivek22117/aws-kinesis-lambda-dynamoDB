package com.ddsolutions.stream.service;

import com.ddsolutions.stream.db.DynamoDBProcessing;
import com.ddsolutions.stream.domain.Event;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.domain.Venue;
import com.ddsolutions.stream.utility.JsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

@RunWith(MockitoJUnitRunner.class)
public class DDBPersistenceServiceTest {

    private DDBPersistenceService ddbPersistenceService;

    @Mock
    private DynamoDBProcessing processing;

    @Mock
    private JsonUtility jsonUtility;

    @Before
    public void setUp() {

        ddbPersistenceService = new DDBPersistenceService(processing, jsonUtility);
    }

    @Test
    public void shouldPersist() throws JsonProcessingException {

        ddbPersistenceService.processRecord(createRSVPRecord());
    }

    private RSVPEventRecord createRSVPRecord() {
        RSVPEventRecord record = new RSVPEventRecord();

        record.setRsvp_id(554433221);
        record.setEvent(createEvent());
        record.setMtime(Instant.now().toEpochMilli());
        record.setVenue(createVenue());

        return record;
    }

    private Venue createVenue() {
        Venue venue = new Venue();
        venue.setLat(45.233);
        venue.setLon(26.98775);
        venue.setVenue_id(1122334);

        return venue;
    }

    private Event createEvent() {
        Event event = new Event();
        event.setEvent_id("don2244");
        return event;
    }
}