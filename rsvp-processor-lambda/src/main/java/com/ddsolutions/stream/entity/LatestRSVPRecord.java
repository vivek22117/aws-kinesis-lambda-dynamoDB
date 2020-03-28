package com.ddsolutions.stream.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;
import java.util.Set;

@DynamoDBTable(tableName = "rsvp-record-processor-table")
public class LatestRSVPRecord implements Serializable {

    @DynamoDBHashKey(attributeName = "rsvp_id")
    private String rsvp_id;

    @DynamoDBRangeKey(attributeName = "rsvp_makeTime")
    private String rsvp_makeTime;

    @DynamoDBAttribute(attributeName = "created_time")
    private String created_time;

    @DynamoDBAttribute(attributeName = "rsvp_record")
    private Set<String> rsvp_record;

    @DynamoDBAttribute(attributeName = "rsvp_with_event_id")
    private String rsvp_with_event_id;

    @DynamoDBAttribute(attributeName = "rsvp_with_venue_id")
    private String rsvp_with_venue_id;

    @DynamoDBAttribute(attributeName = "expiry_time")
    private long expiry_time;


    public String getRsvp_id() {
        return rsvp_id;
    }

    public void setRsvp_id(String rsvp_id) {
        this.rsvp_id = rsvp_id;
    }

    public String getRsvp_makeTime() {
        return rsvp_makeTime;
    }

    public void setRsvp_makeTime(String rsvp_makeTime) {
        this.rsvp_makeTime = rsvp_makeTime;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public Set<String> getRsvp_record() {
        return rsvp_record;
    }

    public void setRsvp_record(Set<String> rsvp_record) {
        this.rsvp_record = rsvp_record;
    }

    public String getRsvp_with_event_id() {
        return rsvp_with_event_id;
    }

    public void setRsvp_with_event_id(String rsvp_with_event_id) {
        this.rsvp_with_event_id = rsvp_with_event_id;
    }

    public String getRsvp_with_venue_id() {
        return rsvp_with_venue_id;
    }

    public void setRsvp_with_venue_id(String rsvp_with_venue_id) {
        this.rsvp_with_venue_id = rsvp_with_venue_id;
    }

    public long getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(long expiry_time) {
        this.expiry_time = expiry_time;
    }

    @Override
    public String toString() {
        return "LatestRSVPRecord{" +
                "rsvp_Id='" + rsvp_id + '\'' +
                ", rsvp_makeTime='" + rsvp_makeTime + '\'' +
                ", created_time='" + created_time + '\'' +
                ", rsvp_record=" + rsvp_record +
                ", rsvp_with_event_id='" + rsvp_with_event_id + '\'' +
                ", rsvp_with_venue_id='" + rsvp_with_venue_id + '\'' +
                ", expiry_time='" + expiry_time + '\'' +
                '}';
    }

    //    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"RSVPVenueIndex", "RSVPEventIndex"})
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "RSVPEventIndex", attributeName = "rsvp_with_event_id")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "RSVPVenueIndex", attributeName = "rsvp_with_venue_id")

}
