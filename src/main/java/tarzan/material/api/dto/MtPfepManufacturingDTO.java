package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;


/**
 * description
 *
 * @author HAND-MC 2019/08/16 15:12
 */
public class MtPfepManufacturingDTO implements Serializable {

    private static final long serialVersionUID = 632898323166663291L;

    @ApiModelProperty("主键ID，表示唯一一条记录")
    private String kid;

    @ApiModelProperty("pfep类型")
    private String keyType;

    @ApiModelProperty(value = "物料主键")
    private String materialId;

    @ApiModelProperty(value = "物料类别主键")
    private String materialCategoryId;

    @ApiModelProperty(value = "站点主键")
    private String siteId;

    @ApiModelProperty(value = "物料类别站点主键")
    private String materialCategorySiteId;

    @ApiModelProperty(value = "组织类型")
    private String organizationType;

    @ApiModelProperty(value = "组织")
    private String organizationId;

    @ApiModelProperty(value = "默认装配清单")
    private String defaultBomId;

    @ApiModelProperty(value = "默认工艺路线")
    private String defaultRoutingId;

    @ApiModelProperty(value = "投料限制类型")
    private String issueControlType;

    @ApiModelProperty(value = "投料限制值")
    private Double issueControlQty;

    @ApiModelProperty(value = "完工限制类型")
    private String completeControlType;

    @ApiModelProperty(value = "完工限制值")
    private Double completeControlQty;

    @ApiModelProperty(value = "损耗类型")
    private String attritionControlType;

    @ApiModelProperty(value = "损耗值")
    private Double attritionControlQty;

    @ApiModelProperty(value = "是否工序装配", required = true)
    @NotBlank
    private String operationAssembleFlag;

    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    private String enableFlag;

    @ApiModelProperty("扩展属性集合")
    private List<MtExtendAttrDTO3> mtPfepManufacturingAttrs;

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

    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public List<MtExtendAttrDTO3> getMtPfepManufacturingAttrs() {
        return mtPfepManufacturingAttrs;
    }

    public void setMtPfepManufacturingAttrs(List<MtExtendAttrDTO3> mtPfepManufacturingAttrs) {
        this.mtPfepManufacturingAttrs = mtPfepManufacturingAttrs;
    }
}
