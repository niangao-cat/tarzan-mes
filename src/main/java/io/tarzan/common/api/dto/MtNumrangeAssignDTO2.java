package io.tarzan.common.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtNumrangeAssignDTO2 implements Serializable {
    
    private static final long serialVersionUID = 2753132741916891518L;

    @ApiModelProperty("号码段分配表主键")
    private String numrangeAssignId;
    
    @ApiModelProperty(value = "号码段定义表主键", required = true)
    @NotBlank
    private String numrangeId;
    
    @ApiModelProperty(value = "编码对象ID",required = true)
    @NotBlank
    private String objectId;
    
    @ApiModelProperty(value = "对象类型ID")
    private String objectTypeId;
    
    @ApiModelProperty(value = "对象类型编码")
    private String objectTypeCode;
    
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    
    @ApiModelProperty(value = "站点编码")
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
