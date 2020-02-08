package com.ddsolutions.stream.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.ddsolutions.stream.domain.RSVPEventRecord;
import com.ddsolutions.stream.utility.AWSUtil;
import com.ddsolutions.stream.utility.JsonUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ddsolutions.stream.utility.PropertyLoader.getInstance;

public class SQSService {
    private Logger logger = LogManager.getLogger(SQSService.class);

    private AmazonSQS amazonSQS;
    private CloudwatchOperation cloudwatchOperation;
    private JsonUtility jsonUtility;
    private boolean dumpToBackoutQueue = false;

    private static final String queueEndpoint = getInstance().getPropValues("SQS.ENDPOINT");
    private static final String backoutQueueEndpoint = getInstance().getPropValues("ERROR.SQS.ENDPOINT");


    public SQSService() {
        this(AWSUtil.getSQSClient(), new CloudwatchOperation(), new JsonUtility());
    }

    public SQSService(AmazonSQS amazonSQS, CloudwatchOperation cloudwatchOperation, JsonUtility jsonUtility) {
        this.amazonSQS = amazonSQS;
        this.cloudwatchOperation = cloudwatchOperation;
        this.jsonUtility = jsonUtility;
    }

    public void publishMessage(String data, String sqsQueueName) {
        try {
            amazonSQS.sendMessage(createMessageRequest(data, sqsQueueName));
        } catch (AmazonSQSException e) {
            cloudwatchOperation.putMetricData("NumberOfFailedRecordsForSQSSend", "RSVPRecordProcessor",
                    "SendToSQS");
            logger.error("Error Occurred: {}. Failed to send record to Queue. Record: {}", e.getMessage(), data, e);
        }
    }

    public void deleteMessage(Message message, String sqsQueueName) {
        amazonSQS.deleteMessage(new DeleteMessageRequest()
                .withQueueUrl(sqsQueueName)
                .withReceiptHandle(message.getReceiptHandle()));
    }

    public void scheduleTaskToConsumeSQSMessages() throws IOException {

        String consumeFromQueue = getQueueNameToConsumeMessages();
        int totalMessageCount = getMessageCount(consumeFromQueue);
        logger.debug("Total records from SQS : {}", totalMessageCount);
        int iterator = 0;
        while (iterator < totalMessageCount) {
            List<Message> messages = getMessage(consumeFromQueue);
            if (messages.isEmpty()) {
                break;
            }
            for (Message message : messages) {
                RSVPEventRecord sqsApiRecord = jsonUtility.convertFromJson(message.getBody(), RSVPEventRecord.class);

                deleteMessage(message, consumeFromQueue);
            }
            iterator += messages.size();
        }
    }

    public List<Message> getMessage(String queueName) {
        ReceiveMessageResult receiveMessageResult = amazonSQS.receiveMessage(queueName);
        return receiveMessageResult.getMessages();
    }

    private SendMessageRequest createMessageRequest(String data, String sqsQueueName) {
        return new SendMessageRequest().withQueueUrl(sqsQueueName).withMessageBody(data);
    }

    private int getMessageCount(String queueName) {
        List<String> attributeNames = new ArrayList<>();
        GetQueueAttributesRequest attributesRequest = new GetQueueAttributesRequest();
        attributesRequest.withQueueUrl(queueName);
        attributeNames.add("ApproximateNumberOfMessages");
        attributesRequest.setAttributeNames(attributeNames);
        GetQueueAttributesResult getQueueAttributesResult = amazonSQS.getQueueAttributes(attributesRequest);
        String approximateNumberOfMessages = getQueueAttributesResult
                .getAttributes().get("ApproximateNumberOfMessages");
        return Integer.parseInt(approximateNumberOfMessages);
    }

    private String getQueueNameToConsumeMessages() {
        String consumeFromQueue;
        if (dumpToBackoutQueue) {
            consumeFromQueue = queueEndpoint;
        } else {
            consumeFromQueue = backoutQueueEndpoint;
        }
        return consumeFromQueue;
    }

}
