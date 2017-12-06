package com.mobilophilia.masterdairy.manager.beans;

/**
 * Created by mukesh on 02/12/17.
 */

public class UpdateEntryBean {

    private String updateCode;
    private String updateClr;
    private String updateFat;
    private String updatePrice;
    private String updateLtr;
    private String updateTotal;
    private String updatedSnf;
    private String currentTime;
    private String agentId;
    private int updateTimeInterval;

    public String getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(String updatecode) {
        this.updateCode = updatecode;
    }

    public String getUpdateClr() {
        return updateClr;
    }

    public void setUpdateClr(String updateClr) {
        this.updateClr = updateClr;
    }

    public String getUpdateFat() {
        return updateFat;
    }

    public void setUpdateFat(String updateFat) {
        this.updateFat = updateFat;
    }

    public String getUpdatePrice() {
        return updatePrice;
    }

    public void setUpdatePrice(String updatePrice) {
        this.updatePrice = updatePrice;
    }

    public String getUpdateLtr() {
        return updateLtr;
    }

    public void setUpdateLtr(String updateLtr) {
        this.updateLtr = updateLtr;
    }

    public String getUpdateTotal() {
        return updateTotal;
    }

    public void setUpdateTotal(String updateTotal) {
        this.updateTotal = updateTotal;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String updateTime) {
        this.currentTime = updateTime;
    }

    public int getUpdateTimeInterval() {
        return updateTimeInterval;
    }

    public void setUpdateTimeInterval(int updateTimeInterval) {
        this.updateTimeInterval = updateTimeInterval;
    }
    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getUpdatedSnf() {
        return updatedSnf;
    }

    public void setUpdatedSnf(String updatedSnf) {
        this.updatedSnf = updatedSnf;
    }

}
