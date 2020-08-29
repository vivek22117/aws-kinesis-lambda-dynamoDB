package com.ddsolutions.stream.lambda;


import com.amazonaws.services.kinesis.clientlibrary.types.UserRecord;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.service.CloudwatchOperation;
import com.ddsolutions.stream.service.DDBPersistenceService;
import com.ddsolutions.stream.utility.GzipUtility;
import com.ddsolutions.stream.utility.JsonUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class KinesisStreamProcessor {
    private static final Logger LOGGER = LogManager.getLogger(KinesisStreamProcessor.class);
    private static final String METRIC_NAME = "FailedRecordsCount";
    private static final String DIMENSION_NAME = "ProcessingFailed";
    private static final String DIMENSION_VALUE = "ProcessingFailedCount";

    private JsonUtility jsonUtility;
    private DDBPersistenceService ddbPersistenceService;
    private CloudwatchOperation cloudwatchOperation;

    public KinesisStreamProcessor() {
        this(new JsonUtility(), new DDBPersistenceService(), new CloudwatchOperation());
    }

    private KinesisStreamProcessor(JsonUtility jsonUtility, DDBPersistenceService ddbPersistenceService,
                                   CloudwatchOperation cloudwatchOperation) {
        this.jsonUtility = jsonUtility;
        this.ddbPersistenceService = ddbPersistenceService;
        this.cloudwatchOperation = cloudwatchOperation;
    }

    public void processLatestReportedEvent(KinesisEvent kinesisEvent) {
        LOGGER.info("Processing started for kinesis event......");
        List<KinesisEvent.KinesisEventRecord> records = kinesisEvent.getRecords();

        try {
            records.stream()
                    .map(x -> UserRecord.deaggregate(Collections.singletonList(x.getKinesis())))
                    .flatMap(List::stream)
                    .map(record -> record.getData().array())
                    .map(GzipUtility::decompressData).filter(Objects::nonNull)
                    .map(GzipUtility::deserializeData).filter(Objects::nonNull)
                    .map(this::convertToObject).filter(Objects::nonNull)
                    .forEach(rsvpEvent -> {
                        try {
                            ddbPersistenceService.processRecord(rsvpEvent);
                        } catch (Exception ex) {
                            LOGGER.error("RSVP event processing failed {}", rsvpEvent, ex);
                        }
                    });
        } catch (Exception ex) {
            LOGGER.error("Processing failed for kinesis event discarding bad data....", ex);
        }
    }

    private RSVPEventRecord convertToObject(String data) {
        LOGGER.info("Received data is... {}", data);
        try {
            return jsonUtility.convertFromJson(data, RSVPEventRecord.class);
        } catch (IOException e) {
            LOGGER.error("Json conversion failed for {}", data, e);
            cloudwatchOperation.putMetricData(METRIC_NAME, DIMENSION_NAME, DIMENSION_VALUE);
            return null;
        }
    }
}
