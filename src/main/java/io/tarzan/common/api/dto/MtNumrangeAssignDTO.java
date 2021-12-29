package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtNumrangeAssignDTO implements Serializable {
    private static final long serialVersionUID = 974287198623585037L;

    private String numrangeAssignId;
    private String numrangeId;
    private String objectId;
    private String objectTypeId;
    private String objectTypeCode;
    private String siteId;
    private String site;

    public String getNumrangeAssignId() {
        return numrangeAssignId;
    }

    public void setNumrangeAssignId(String numrangeAssignId) {
        this.numrangeAssignId = numrangeAssignId;
    }

    public String getNumrangeId() {
        return numrangeId;
    }

    public void setNumrangeId(String numrangeId) {
        this.numrangeId = numrangeId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
