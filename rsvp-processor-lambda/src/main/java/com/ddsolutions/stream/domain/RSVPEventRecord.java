package com.ddsolutions.stream.domain;

public class RSVPEventRecord {
    private Venue venue;
    private String visibility;
    private String response;
    private int guests;
    private Member member;
    private int rsvp_id;
    private long mtime;
    private Event event;
    private Group group;

    public RSVPEventRecord() {
        super();
    }

    public RSVPEventRecord(Venue venue, String visibility, String response,
                           int guests, Member member, int rsvp_id, long mtime, Event event, Group group) {
        this.venue = venue;
        this.visibility = visibility;
        this.response = response;
        this.guests = guests;
        this.member = member;
        this.rsvp_id = rsvp_id;
        this.mtime = mtime;
        this.event = event;
        this.group = group;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getRsvp_id() {
        return rsvp_id;
    }

    public void setRsvp_id(int rsvp_id) {
        this.rsvp_id = rsvp_id;
    }

    public long getMtime() {
        return mtime;
    }

    public void setMtime(long mtime) {
        this.mtime = mtime;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "RSVPEventRecord{" +
                "venue=" + venue +
                ", visibility='" + visibility + '\'' +
                ", response='" + response + '\'' +
                ", guests=" + guests +
                ", member=" + member +
                ", rsvp_id=" + rsvp_id +
                ", mtime=" + mtime +
                ", event=" + event +
                ", group=" + group +
                '}';
    }
}
