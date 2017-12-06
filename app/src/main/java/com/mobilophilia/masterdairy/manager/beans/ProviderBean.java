package com.mobilophilia.masterdairy.manager.beans;

/**
 * Created by mukesh on 13/09/17.
 */

public class ProviderBean {

    private int id;
    private int contentType;
    private String headerText;

    private String dariyName;
    private String ltr;
    private String avgFat;
    private String avgSnf;
    private String avgPrice;
    private String avgClr;
    private String totalPrice;

    public String getOtherEx() {
        return otherEx;
    }

    public void setOtherEx(String otherEx) {
        this.otherEx = otherEx;
    }

    public String getChillingEx() {
        return chillingEx;
    }

    public void setChillingEx(String chillingEx) {
        this.chillingEx = chillingEx;
    }

    private String otherEx;
    private String chillingEx;

    public String getLtr() {
        return ltr;
    }

    public void setLtr(String ltr) {
        this.ltr = ltr;
    }

    public String getAvgFat() {
        return avgFat;
    }

    public void setAvgFat(String avgFat) {
        this.avgFat = avgFat;
    }

    public String getAvgSnf() {
        return avgSnf;
    }

    public void setAvgSnf(String avgSnf) {
        this.avgSnf = avgSnf;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getAvgClr() {
        return avgClr;
    }

    public void setAvgClr(String avgClr) {
        this.avgClr = avgClr;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDariyName() {
        return dariyName;
    }

    public void setDariyName(String dariyName) {
        this.dariyName = dariyName;
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



}
