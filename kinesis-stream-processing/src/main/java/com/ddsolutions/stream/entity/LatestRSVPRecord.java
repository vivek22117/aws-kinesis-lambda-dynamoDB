package com.ddsolutions.stream.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Set;

@DynamoDBTable(tableName = "rsvp_record_processor_table")
public class LatestRSVPRecord {
    private String rsvpId;
    private String rsvpMakeTime;
    private String createdDate;
    private Set<String> rsvpEventRecord;
    private String rsvpEventId;
    private String rsvpVenueId;
    private String expiryTime;


    @DynamoDBHashKey(attributeName = "rsvp_Id")
    public String getRsvpId() {
        return rsvpId;
    }
    public void setRsvpId(String rsvpId) {
        this.rsvpId = rsvpId;
    }

    @DynamoDBRangeKey(attributeName = "rsvp_makeTime")
    public String getRsvpMakeTime() {
        return rsvpMakeTime;
    }
    public void setRsvpMakeTime(String rsvpMakeTime) {
        this.rsvpMakeTime = rsvpMakeTime;
    }

    @DynamoDBAttribute(attributeName = "created_time")
    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @DynamoDBAttribute(attributeName = "rsvp_record")
    public Set<String> getRsvpEventRecord() {
        return rsvpEventRecord;
    }
    public void setRsvpEventRecord(Set<String> rsvpEventRecord) {
        this.rsvpEventRecord = rsvpEventRecord;
    }

    @DynamoDBAttribute(attributeName = "rsvp_with_event_id")
    public String getRsvpEventId() {
        return rsvpEventId;
    }
    public void setRsvpEventId(String rsvpEventId) {
        this.rsvpEventId = rsvpEventId;
    }

    @DynamoDBAttribute(attributeName = "rsvp_with_venue_id")
    public String getRsvpVenueId() {
        return rsvpVenueId;
    }
    public void setRsvpVenueId(String rsvpVenueId) {
        this.rsvpVenueId = rsvpVenueId;
    }

    @DynamoDBAttribute(attributeName = "expiry_time")
    public String getExpiryTime() {
        return expiryTime;
    }
    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }
}
