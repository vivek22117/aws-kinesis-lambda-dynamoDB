package com.ddsolutions.stream.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.ddsolutions.stream.utility.AWSUtil;
import com.ddsolutions.stream.utility.JsonUtility;
import com.ddsolutions.stream.utility.PropertyLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.ddsolutions.stream.utility.PropertyLoader.getInstance;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class SNSPublisherService {
    private Logger logger = LogManager.getLogger(SNSPublisherService.class);

    private AmazonSNS amazonSNS;

    public SNSPublisherService() {
        this(AWSUtil.getSNSClient());
    }

    public SNSPublisherService(AmazonSNS amazonSNS) {
        this.amazonSNS = amazonSNS;
    }

    public void sendAlarmNotification(String data, Exception exception) throws JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("h:mm a MM/dd/YY");

        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        map.put("errorStackTrace", getStackTrace(exception));
        map.put("data", data);
        String messagePreFix = "AppName: " + PropertyLoader.getInstance().getPropValues("AppName") + "\n\n"
                + "Group: " + getInstance().getPropValues("GroupName") + "\n\n"
                + "Time: " + format.format(Date.from(Instant.now())) + "\n\n";
        sendNotification(messagePreFix.concat(new JsonUtility().convertToJson(map)));
    }

    private void sendNotification(String message) {
        List<String> messageIds = new ArrayList<>();
        try {
            String arn = getInstance().getPropValues("SNS_TOPIC_ARN");
            PublishResult result = amazonSNS.publish(arn, message);
            messageIds.add(result.getMessageId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
