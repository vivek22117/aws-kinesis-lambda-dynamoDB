package com.ddsolutions.stream.domain;

public class MyCompany {

    private String id;
    private String companyName;
    private String companyAddress;
    private String companyBeginDate;
    private String companyEndDate;
    private boolean isActive;


    public String getCompanyBeginDate() {
        return companyBeginDate;
    }

    public void setCompanyBeginDate(String companyBeginDate) {
        this.companyBeginDate = companyBeginDate;
    }

    public String getCompanyEndDate() {
        return companyEndDate;
    }

    public void setCompanyEndDate(String companyEndDate) {
        this.companyEndDate = companyEndDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "MyCompany{" +
                "id='" + id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyBeginDate='" + companyBeginDate + '\'' +
                ", companyEndDate='" + companyEndDate + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
