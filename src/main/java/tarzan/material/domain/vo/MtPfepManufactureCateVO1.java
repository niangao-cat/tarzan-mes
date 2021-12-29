package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/18 15:01
 * @Description:
 */
public class MtPfepManufactureCateVO1 implements Serializable {
    private static final long serialVersionUID = 5467100498574890518L;


    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String pfepManufacturingCategoryId;

    @ApiModelProperty(value = "物料类别站点主键，标识唯一物料类别站点分配数据，限定为生产站点", required = true)
    private String materialCategorySiteId;

    @ApiModelProperty(value = "组织类型，可选计划站点下区域、生产线、工作单元等类型")
    private String organizationType;

    @ApiModelProperty(value = "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")
    private String organizationId;

    @ApiModelProperty(value = "默认装配清单")
    private String defaultBomId;

    @ApiModelProperty(value = "默认工艺路线")
    private String defaultRoutingId;

    @ApiModelProperty(value = "投料限制类型，如数量限制、百分比限制等")
    private String issueControlType;

    @ApiModelProperty(value = "投料限制值")
    private Double issueControlQty;

    @ApiModelProperty(value = "完工限制类型")
    private String completeControlType;

    @ApiModelProperty(value = "完工限制值")
    private Double completeControlQty;

    @ApiModelProperty(value = "损耗类型，如数量限制、百分比限制等")
    private String attritionControlType;

    @ApiModelProperty(value = "损耗值")
    private Double attritionControlQty;

    @ApiModelProperty(value = "是否工序装配")
    private String operationAssembleFlag;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "物料类型唯一标识")
    private String materialCategoryId;

    @ApiModelProperty(value = "站点唯一标识")
    private String siteId;

    public String getPfepManufacturingCategoryId() {
        return pfepManufacturingCategoryId;
    }

    public void setPfepManufacturingCategoryId(String pfepManufacturingCategoryId) {
        this.pfepManufacturingCategoryId = pfepManufacturingCategoryId;
    }

    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDefaultBomId() {
        return defaultBomId;
    }

    public void setDefaultBomId(String defaultBomId) {
        this.defaultBomId = defaultBomId;
    }

    public String getDefaultRoutingId() {
        return defaultRoutingId;
    }

    public void setDefaultRoutingId(String defaultRoutingId) {
        this.defaultRoutingId = defaultRoutingId;
    }

    public String getIssueControlType() {
        return issueControlType;
    }

    public void setIssueControlType(String issueControlType) {
        this.issueControlType = issueControlType;
    }

    public Double getIssueControlQty() {
        return issueControlQty;
    }

    public void setIssueControlQty(Double issueControlQty) {
        this.issueControlQty = issueControlQty;
    }

    public String getCompleteControlType() {
        return completeControlType;
    }

    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }

    public Double getCompleteControlQty() {
        return completeControlQty;
    }

    public void setCompleteControlQty(Double completeControlQty) {
        this.completeControlQty = completeControlQty;
    }

    public String getAttritionControlType() {
        return attritionControlType;
    }

    public void setAttritionControlType(String attritionControlType) {
        this.attritionControlType = attritionControlType;
    }

    public Double getAttritionControlQty() {
        return attritionControlQty;
    }

    public void setAttritionControlQty(Double attritionControlQty) {
        this.attritionControlQty = attritionControlQty;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
