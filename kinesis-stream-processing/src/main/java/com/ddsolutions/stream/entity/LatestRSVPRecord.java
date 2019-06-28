package com.ddsolutions.stream.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "RSVPEventTable")
public class LatestRSVPRecord {

    @DynamoDBHashKey(attributeName = "rsvp_Id")
    private String rsvpId;

    @DynamoDBAttribute(attributeName = "rsvp_makeTime")
    private String rsvpMakeTime;

    @DynamoDBRangeKey(attributeName = "created_date")
    private String createdDate;

    @DynamoDBAttribute(attributeName = "rsvp_record")
    private String rsvpEventRecord;

    @DynamoDBAttribute(attributeName = "rsvp_event_id")
    private String rsvpEventId;

    @DynamoDBAttribute(attributeName = "rsvp_venue_id")
    private String rsvpVenueId;


    public String getRsvpId() {
        return rsvpId;
    }

    public void setRsvpId(String rsvpId) {
        this.rsvpId = rsvpId;
    }

    public String getRsvpMakeTime() {
        return rsvpMakeTime;
    }

    public void setRsvpMakeTime(String rsvpMakeTime) {
        this.rsvpMakeTime = rsvpMakeTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getRsvpEventRecord() {
        return rsvpEventRecord;
    }

    public void setRsvpEventRecord(String rsvpEventRecord) {
        this.rsvpEventRecord = rsvpEventRecord;
    }

    public String getRsvpEventId() {
        return rsvpEventId;
    }

    public void setRsvpEventId(String rsvpEventId) {
        this.rsvpEventId = rsvpEventId;
    }

    public String getRsvpVenueId() {
        return rsvpVenueId;
    }

    public void setRsvpVenueId(String rsvpVenueId) {
        this.rsvpVenueId = rsvpVenueId;
    }
}
