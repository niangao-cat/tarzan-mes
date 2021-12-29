package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiao.tang02@hand-china.com
 */

public class MtPfepManufacturingVO implements Serializable {

    private static final long serialVersionUID = 8654012103535542472L;


    @ApiModelProperty("主键ID")
    private String kid;

    @ApiModelProperty("数据类型 material/category")
    private String keyType;

    @ApiModelProperty(value = "物料编码", required = true)
    private String materialCode;

    @ApiModelProperty(value = "物料描述", required = true)
    private String materialName;

    @ApiModelProperty(value = "物料类别编码")
    private String categoryCode;

    @ApiModelProperty(value = "物料类别描述")
    private String categoryDesc;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "生产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "工作单元名称")
    private String workcellName;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "默认装配清单")
    private String defaultBomId;
    @ApiModelProperty(value = "默认装配清单名称")
    private String defaultBomName;
    @ApiModelProperty(value = "默认装配清单版本")
    private String defaultBomRevision;
    @ApiModelProperty(value = "默认工艺路线")
    private String defaultRoutingId;
    @ApiModelProperty(value = "默认工艺路线名称")
    private String defaultRoutingName;
    @ApiModelProperty(value = "默认工艺路线版本")
    private String defaultRoutingRevision;
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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getDefaultBomId() {
        return defaultBomId;
    }

    public void setDefaultBomId(String defaultBomId) {
        this.defaultBomId = defaultBomId;
    }

    public String getDefaultBomName() {
        return defaultBomName;
    }

    public void setDefaultBomName(String defaultBomName) {
        this.defaultBomName = defaultBomName;
    }

    public String getDefaultBomRevision() {
        return defaultBomRevision;
    }

    public void setDefaultBomRevision(String defaultBomRevision) {
        this.defaultBomRevision = defaultBomRevision;
    }

    public String getDefaultRoutingId() {
        return defaultRoutingId;
    }

    public void setDefaultRoutingId(String defaultRoutingId) {
        this.defaultRoutingId = defaultRoutingId;
    }

    public String getDefaultRoutingName() {
        return defaultRoutingName;
    }

    public void setDefaultRoutingName(String defaultRoutingName) {
        this.defaultRoutingName = defaultRoutingName;
    }

    public String getDefaultRoutingRevision() {
        return defaultRoutingRevision;
    }

    public void setDefaultRoutingRevision(String defaultRoutingRevision) {
        this.defaultRoutingRevision = defaultRoutingRevision;
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
}
