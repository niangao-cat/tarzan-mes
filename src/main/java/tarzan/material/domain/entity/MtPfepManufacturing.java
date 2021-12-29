package tarzan.material.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料生产属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@ApiModel("物料生产属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_pfep_manufacturing")
@CustomPrimary
public class MtPfepManufacturing extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PFEP_MANUFACTURING_ID = "pfepManufacturingId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_DEFAULT_BOM_ID = "defaultBomId";
    public static final String FIELD_DEFAULT_ROUTING_ID = "defaultRoutingId";
    public static final String FIELD_ISSUE_CONTROL_TYPE = "issueControlType";
    public static final String FIELD_ISSUE_CONTROL_QTY = "issueControlQty";
    public static final String FIELD_COMPLETE_CONTROL_TYPE = "completeControlType";
    public static final String FIELD_COMPLETE_CONTROL_QTY = "completeControlQty";
    public static final String FIELD_ATTRITION_CONTROL_TYPE = "attritionControlType";
    public static final String FIELD_ATTRITION_CONTROL_QTY = "attritionControlQty";
    public static final String FIELD_OPERATION_ASSEMBLE_FLAG = "operationAssembleFlag";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @Where
    private String pfepManufacturingId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系，限定为生产站点", required = true)
    @NotBlank
    @Where
    private String materialSiteId;
    @ApiModelProperty(value = "组织类型，可选计划站点下区域、生产线、工作单元等类型")
    @Where
    private String organizationType;
    @ApiModelProperty(value = "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")
    @Where
    private String organizationId;
    @ApiModelProperty(value = "默认装配清单")
    @Where
    private String defaultBomId;
    @ApiModelProperty(value = "默认工艺路线")
    @Where
    private String defaultRoutingId;
    @ApiModelProperty(value = "投料限制类型，如数量限制、百分比限制等")
    @Where
    private String issueControlType;
    @ApiModelProperty(value = "投料限制值")
    @Where
    private Double issueControlQty;
    @ApiModelProperty(value = "完工限制类型")
    @Where
    private String completeControlType;
    @ApiModelProperty(value = "完工限制值")
    @Where
    private Double completeControlQty;
    @ApiModelProperty(value = "损耗类型，如数量限制、百分比限制等")
    @Where
    private String attritionControlType;
    @ApiModelProperty(value = "损耗值")
    @Where
    private Double attritionControlQty;
    @ApiModelProperty(value = "是否工序装配")
    @Where
    private String operationAssembleFlag;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 主键ID，表示唯一一条记录
     */
    public String getPfepManufacturingId() {
        return pfepManufacturingId;
    }

    public void setPfepManufacturingId(String pfepManufacturingId) {
        this.pfepManufacturingId = pfepManufacturingId;
    }

    /**
     * @return 物料站点主键，标识唯一物料站点对应关系，限定为生产站点
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 组织类型，可选计划站点下区域、生产线、工作单元等类型
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 默认装配清单
     */
    public String getDefaultBomId() {
        return defaultBomId;
    }

    public void setDefaultBomId(String defaultBomId) {
        this.defaultBomId = defaultBomId;
    }

    /**
     * @return 默认工艺路线
     */
    public String getDefaultRoutingId() {
        return defaultRoutingId;
    }

    public void setDefaultRoutingId(String defaultRoutingId) {
        this.defaultRoutingId = defaultRoutingId;
    }

    /**
     * @return 投料限制类型，如数量限制、百分比限制等
     */
    public String getIssueControlType() {
        return issueControlType;
    }

    public void setIssueControlType(String issueControlType) {
        this.issueControlType = issueControlType;
    }

    /**
     * @return 投料限制值
     */
    public Double getIssueControlQty() {
        return issueControlQty;
    }

    public void setIssueControlQty(Double issueControlQty) {
        this.issueControlQty = issueControlQty;
    }

    /**
     * @return 完工限制类型
     */
    public String getCompleteControlType() {
        return completeControlType;
    }

    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }

    /**
     * @return 完工限制值
     */
    public Double getCompleteControlQty() {
        return completeControlQty;
    }

    public void setCompleteControlQty(Double completeControlQty) {
        this.completeControlQty = completeControlQty;
    }

    /**
     * @return 损耗类型，如数量限制、百分比限制等
     */
    public String getAttritionControlType() {
        return attritionControlType;
    }

    public void setAttritionControlType(String attritionControlType) {
        this.attritionControlType = attritionControlType;
    }

    /**
     * @return 损耗值
     */
    public Double getAttritionControlQty() {
        return attritionControlQty;
    }

    public void setAttritionControlQty(Double attritionControlQty) {
        this.attritionControlQty = attritionControlQty;
    }

    /**
     * @return 是否工序装配
     */
    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
