package tarzan.actual.domain.entity;

import java.io.Serializable;

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
 * 装配确认实绩历史，指示执行作业组件材料的装配和确认历史记录情况
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("装配确认实绩历史，指示执行作业组件材料的装配和确认历史记录情况")

@ModifyAudit

@Table(name = "mt_assemble_confirm_actual_his")
@CustomPrimary
public class MtAssembleConfirmActualHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_CONFIRM_ACTUAL_HIS_ID = "assembleConfirmActualHisId";
    public static final String FIELD_ASSEMBLE_CONFIRM_ACTUAL_ID = "assembleConfirmActualId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_COMPONENT_TYPE = "componentType";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_ASSEMBLE_EXCESS_FLAG = "assembleExcessFlag";
    public static final String FIELD_ASSEMBLE_ROUTER_TYPE = "assembleRouterType";
    public static final String FIELD_SUBSTITUTE_FLAG = "substituteFlag";
    public static final String FIELD_BYPASS_FLAG = "bypassFlag";
    public static final String FIELD_BYPASS_BY = "bypassBy";
    public static final String FIELD_CONFIRM_FLAG = "confirmFlag";
    public static final String FIELD_CONFIRMED_BY = "confirmedBy";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -6991927548930523297L;

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
    @ApiModelProperty("装配确认实绩历史ID")
    @Id
    @Where
    private String assembleConfirmActualHisId;
    @ApiModelProperty(value = "装配确认实绩", required = true)
    @NotBlank
    @Where
    private String assembleConfirmActualId;
    @ApiModelProperty(value = "EO主键ID，标识实绩对应的唯一执行作业", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "实际装配物料ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "实际装配OPERATION，实绩装配工艺与需求步骤工艺不一致时也判断为强制装配")
    @Where
    private String operationId;
    @ApiModelProperty(value = "实际装配组件类型，如装配、拆卸、联产品等", required = true)
    @NotBlank
    @Where
    private String componentType;
    @ApiModelProperty(value = "非强制装配时物料对应装配清单行ID")
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "装配时执行作业引用的装配清单")
    @Where
    private String bomId;
    @ApiModelProperty(value = "非强制装配时物料对应组件装配步骤需求")
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "强制装配标识，“Y”代表该记录为强制装配，不属于原装配清单部分")
    @Where
    private String assembleExcessFlag;
    @ApiModelProperty(value = "包括NC和特殊工艺路线装配，均属于强制装配")
    @Where
    private String assembleRouterType;
    @ApiModelProperty(value = "替代装配标识，“Y”代表装配的是替代件，如果是替代")
    @Where
    private String substituteFlag;
    @ApiModelProperty(value = "装配遗留标识，“Y”代表用户主动遗留")
    @Where
    private String bypassFlag;
    @ApiModelProperty(value = "遗留人")
    @Where
    private String bypassBy;
    @ApiModelProperty(value = "装配确认标识，“Y”代表该行记录以被确认")
    @Where
    private String confirmFlag;
    @ApiModelProperty(value = "确认人")
    @Where
    private String confirmedBy;
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
     * @return 装配确认实绩历史ID
     */
    public String getAssembleConfirmActualHisId() {
        return assembleConfirmActualHisId;
    }

    public void setAssembleConfirmActualHisId(String assembleConfirmActualHisId) {
        this.assembleConfirmActualHisId = assembleConfirmActualHisId;
    }

    /**
     * @return 装配确认实绩
     */
    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    /**
     * @return EO主键ID，标识实绩对应的唯一执行作业
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 实际装配物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 实际装配OPERATION，实绩装配工艺与需求步骤工艺不一致时也判断为强制装配
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 实际装配组件类型，如装配、拆卸、联产品等
     */
    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * @return 非强制装配时物料对应装配清单行ID
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 装配时执行作业引用的装配清单
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    /**
     * @return 非强制装配时物料对应组件装配步骤需求
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 强制装配标识，“Y”代表该记录为强制装配，不属于原装配清单部分
     */
    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    /**
     * @return 包括NC和特殊工艺路线装配，均属于强制装配
     */
    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    /**
     * @return 替代装配标识，“Y”代表装配的是替代件，如果是替代
     */
    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    /**
     * @return 装配遗留标识，“Y”代表用户主动遗留
     */
    public String getBypassFlag() {
        return bypassFlag;
    }

    public void setBypassFlag(String bypassFlag) {
        this.bypassFlag = bypassFlag;
    }

    /**
     * @return 遗留人
     */
    public String getBypassBy() {
        return bypassBy;
    }

    public void setBypassBy(String bypassBy) {
        this.bypassBy = bypassBy;
    }

    /**
     * @return 装配确认标识，“Y”代表该行记录以被确认
     */
    public String getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    /**
     * @return 确认人
     */
    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

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
