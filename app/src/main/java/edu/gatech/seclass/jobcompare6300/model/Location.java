package edu.gatech.seclass.jobcompare6300.model;

public class Location {
    private int locationId = -1;
    private String city;
    private String state;
    private int costOfLivingIndex;

    public Location() {
    }

    public Location(int locationId, String city, String state, int costOfLivingIndex) {
        this.locationId = locationId;
        this.city = city;
        this.state = state;
        this.costOfLivingIndex = costOfLivingIndex;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCostOfLivingIndex() {
        return costOfLivingIndex;
    }

    public void setCostOfLivingIndex(int costOfLivingIndex) {
        this.costOfLivingIndex = costOfLivingIndex;
    }
}
