package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


public class MtNumrangeAssignVO implements Serializable {

    private static final long serialVersionUID = -7132793654031101415L;

    @ApiModelProperty(value = "号码段定义表主键")
    private String numrangeId;

    @ApiModelProperty(value = "号段组号")
    private String numrangeGroup;

    @ApiModelProperty(value = "编码对象主键")
    private String objectId; 

    @ApiModelProperty(value = "号段描述")
    private String numDescription;

    @ApiModelProperty(value = "号段示例")
    private String numExample;

    @ApiModelProperty(value = "外部输入编码标识")
    private String outsideNumFlag;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "编码对象编码")
    private String objectCode;
    
    @ApiModelProperty(value = "编码对象名称")
    private String objectName;

    @ApiModelProperty(value = "号码段分配主键")
    private String numrangeAssignId;

    @ApiModelProperty(value = "编码对象类型主键")
    private String objectTypeId;

    @ApiModelProperty(value = "编码对象类型编码")
    private String objectTypeCode;

    @ApiModelProperty(value = "编码对象类型描述")
    private String objectTypeDesc;

    @ApiModelProperty(value = "站点主键")
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    private String site;

    @ApiModelProperty(value = "站点描述")
    private String siteDesc;

    public String getNumrangeId() {
        return numrangeId;
    }

    public void setNumrangeId(String numrangeId) {
        this.numrangeId = numrangeId;
    }

    public String getNumrangeGroup() {
        return numrangeGroup;
    }

    public void setNumrangeGroup(String numrangeGroup) {
        this.numrangeGroup = numrangeGroup;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getNumDescription() {
        return numDescription;
    }

    public void setNumDescription(String numDescription) {
        this.numDescription = numDescription;
    }

    public String getNumExample() {
        return numExample;
    }

    public void setNumExample(String numExample) {
        this.numExample = numExample;
    }

    public String getOutsideNumFlag() {
        return outsideNumFlag;
    }

    public void setOutsideNumFlag(String outsideNumFlag) {
        this.outsideNumFlag = outsideNumFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getObjectTypeDesc() {
        return objectTypeDesc;
    }

    public void setObjectTypeDesc(String objectTypeDesc) {
        this.objectTypeDesc = objectTypeDesc;
    }

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
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

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }

    public String getNumrangeAssignId() {
        return numrangeAssignId;
    }

    public void setNumrangeAssignId(String numrangeAssignId) {
        this.numrangeAssignId = numrangeAssignId;
    }

}
