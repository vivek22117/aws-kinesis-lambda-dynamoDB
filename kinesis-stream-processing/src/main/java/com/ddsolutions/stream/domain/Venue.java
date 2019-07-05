package com.ddsolutions.stream.domain;

public class Venue {
    private String venue_name;
    private double lon;
    private double lat;
    private int venue_id;

    public Venue() {
    }

    public Venue(String venue_name, double lon, double lat, int venue_id) {
        this.venue_name = venue_name;
        this.lon = lon;
        this.lat = lat;
        this.venue_id = venue_id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(int venue_id) {
        this.venue_id = venue_id;
    }
}
