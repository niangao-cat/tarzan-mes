package tarzan.general.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据收集组关联对象历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@ApiModel("数据收集组关联对象历史表")

@ModifyAudit

@Table(name = "mt_tag_group_object_his")
@CustomPrimary
public class MtTagGroupObjectHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TAG_GROUP_OBJECT_HIS_ID = "tagGroupObjectHisId";
    public static final String FIELD_TAG_GROUP_OBJECT_ID = "tagGroupObjectId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_NC_CODE_ID = "ncCodeId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_ITEM_TYPE = "itemType";
    public static final String FIELD_EVENT_ID = "eventId";
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
    @ApiModelProperty("数据收集组关联对象历史ID")
    @Id
    @Where
    private String tagGroupObjectHisId;
    @ApiModelProperty(value = "数据收集组关联对象ID", required = true)
    @NotBlank
    @Where
    private String tagGroupObjectId;
    @ApiModelProperty(value = "数据收集组ID", required = true)
    @NotBlank
    @Where
    private String tagGroupId;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "工艺ID")
    @Where
    private String operationId;
    @ApiModelProperty(value = "工艺路线ID")
    @Where
    private String routerId;
    @ApiModelProperty(value = "工艺路线步骤ID")
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "工作单元ID")
    @Where
    private String workcellId;
    @ApiModelProperty(value = "WO ID")
    @Where
    private String workOrderId;
    @ApiModelProperty(value = "EO ID")
    @Where
    private String eoId;
    @ApiModelProperty(value = "NC代码ID")
    @Where
    private String ncCodeId;
    @ApiModelProperty(value = "装配清单ID")
    @Where
    private String bomId;
    @ApiModelProperty(value = "装配清单行ID")
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "物料批ID")
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "生产版本")
    @Where
    private String productionVersion;
    @ApiModelProperty(value = "物料类别")
    @Where
    private String itemType;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 数据收集组关联对象历史ID
     */
    public String getTagGroupObjectHisId() {
        return tagGroupObjectHisId;
    }

    public void setTagGroupObjectHisId(String tagGroupObjectHisId) {
        this.tagGroupObjectHisId = tagGroupObjectHisId;
    }

    /**
     * @return 数据收集组关联对象ID
     */
    public String getTagGroupObjectId() {
        return tagGroupObjectId;
    }

    public void setTagGroupObjectId(String tagGroupObjectId) {
        this.tagGroupObjectId = tagGroupObjectId;
    }

    /**
     * @return 数据收集组ID
     */
    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 工艺ID
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 工艺路线ID
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 工艺路线步骤ID
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 工作单元ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return WO ID
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return EO ID
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return NC代码ID
     */
    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    /**
     * @return 装配清单ID
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    /**
     * @return 装配清单行ID
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 物料批ID
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 生产版本
     */
    public String getProductionVersion(){ return productionVersion; }

    public void setProductionVersion(String productionVersion) {this.productionVersion = productionVersion; }

    /**
     * @return 物料类别
     */
    public String getItemType() { return itemType; }

    public void setItemType(String itemType) {this.itemType = itemType; }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
