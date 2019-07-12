package com.ddsolutions.stream.service;

import com.ddsolutions.stream.db.DynamoDBProcessing;
import com.ddsolutions.stream.domain.Event;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.domain.Venue;
import com.ddsolutions.stream.entity.LatestRSVPRecord;
import com.ddsolutions.stream.utility.JsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

@RunWith(MockitoJUnitRunner.class)
public class DDBPersistenceServiceTest {

    private DDBPersistenceService ddbPersistenceService;
    private static final String rsvp_record = "{\"venue\":{\"venue_name\":null,\"lon\":26.98775,\"lat\":45.233,\"" +
            "venue_id\":2999991},\"visibility\":null,\"response\":null,\"guests\":0,\"member\":null,\"" +
            "rsvp_id\":9999991,\"mtime\":1562606610707,\"event\":{\"event_name\":null,\"event_id\":\"1999991\"" +
            ",\"time\":0,\"event_url\":null},\"group\":null}";

    @Mock
    private DynamoDBProcessing processing;

    @Mock
    private JsonUtility jsonUtility;

    @Captor
    private ArgumentCaptor<RSVPEventRecord> rsvpRecord;

    @Captor
    private ArgumentCaptor<LatestRSVPRecord> latestRSVPRecord;

    @Before
    public void setUp() {

        ddbPersistenceService = new DDBPersistenceService(processing, jsonUtility);
    }

    @Test
    public void shouldPersist() throws JsonProcessingException {
        Mockito.when(jsonUtility.convertToJson(rsvpRecord.capture())).thenReturn(rsvp_record);

        ddbPersistenceService.processRecord(createRSVPRecord());

        Mockito.verify(processing).save(latestRSVPRecord.capture());
    }

    private RSVPEventRecord createRSVPRecord() {
        RSVPEventRecord record = new RSVPEventRecord();

        record.setRsvp_id(9999991);
        record.setEvent(createEvent());
        record.setMtime(Instant.now().toEpochMilli());
        record.setVenue(createVenue());

        return record;
    }

    private Venue createVenue() {
        Venue venue = new Venue();
        venue.setLat(45.233);
        venue.setLon(26.98775);
        venue.setVenue_id(2999991);

        return venue;
    }

    private Event createEvent() {
        Event event = new Event();
        event.setEvent_id("1999991");
        return event;
    }
}