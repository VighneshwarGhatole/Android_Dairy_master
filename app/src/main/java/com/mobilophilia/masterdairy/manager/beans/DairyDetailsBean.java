package com.mobilophilia.masterdairy.manager.beans;

/**
 * Created by mukesh on 11/09/17.
 */

public class DairyDetailsBean {

    private int id;
    private String dairyName;
    private String dairyCareBy;
    private String dairyPhone;
    private String createdAt;
    private int contentType;
    private String headerText;
    private String dairyAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDairyName() {
        return dairyName;
    }

    public void setDairyName(String dairyName) {
        this.dairyName = dairyName;
    }

    public String getDairyCareBy() {
        return dairyCareBy;
    }

    public void setDairyCareBy(String dairyCareBy) {
        this.dairyCareBy = dairyCareBy;
    }

    public String getDairyPhone() {
        return dairyPhone;
    }

    public void setDairyPhone(String dairyPhone) {
        this.dairyPhone = dairyPhone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getDairyAddress() {
        return dairyAddress;
    }

    public void setDairyAddress(String dairyAddress) {
        this.dairyAddress = dairyAddress;
    }


}
