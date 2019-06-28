package com.ddsolutions.stream.lambda;


import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.service.DDBPersistenceService;
import com.ddsolutions.stream.utility.JsonUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class KinesisStreamProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(KinesisStreamProcessor.class);

    private JsonUtility jsonUtility;
    private DDBPersistenceService ddbPersistenceService;

    public KinesisStreamProcessor() {
        this(new JsonUtility(), new DDBPersistenceService());
    }

    private KinesisStreamProcessor(JsonUtility jsonUtility, DDBPersistenceService ddbPersistenceService) {
        this.jsonUtility = jsonUtility;
        this.ddbPersistenceService = ddbPersistenceService;
    }

    public void processLatestReportedEvent(KinesisEvent kinesisEvent) {
        LOGGER.info("Processing started for kinesis event......");
        List<KinesisEvent.KinesisEventRecord> records = kinesisEvent.getRecords();
        records.stream()
                .map(record -> new String(record.getKinesis().getData().array()))
                .map(this::convertToObject)
                .filter(Objects::nonNull)
                .forEach(rsvpEvent -> {
                    try {
                        ddbPersistenceService.processRecord(rsvpEvent);
                    } catch (Exception ex) {
                        LOGGER.error("RSVP event processing failed {}", rsvpEvent, ex);
                    }
                });
    }

    private RSVPEventRecord convertToObject(String data) {
        try {
            return jsonUtility.convertFromJson(data, RSVPEventRecord.class);
        } catch (IOException e) {
            LOGGER.error("Json conversion failed for {}", data, e);
        }
        return null;
    }
}
