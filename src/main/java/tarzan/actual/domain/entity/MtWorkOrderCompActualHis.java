package tarzan.actual.domain.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 生产订单组件装配实绩历史，记录生产订单物料和组件实际装配情况变更历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("生产订单组件装配实绩历史，记录生产订单物料和组件实际装配情况变更历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_work_order_comp_actual_his")
@CustomPrimary
public class MtWorkOrderCompActualHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORK_ORDER_COMP_ACTUAL_HIS_ID = "workOrderCompActualHisId";
    public static final String FIELD_WORK_ORDER_COMPONENT_ACTUAL_ID = "workOrderComponentActualId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_ASSEMBLE_QTY = "assembleQty";
    public static final String FIELD_SCRAPPED_QTY = "scrappedQty";
    public static final String FIELD_COMPONENT_TYPE = "componentType";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_ASSEMBLE_EXCESS_FLAG = "assembleExcessFlag";
    public static final String FIELD_ASSEMBLE_ROUTER_TYPE = "assembleRouterType";
    public static final String FIELD_SUBSTITUTE_FLAG = "substituteFlag";
    public static final String FIELD_ACTUAL_FIRST_TIME = "actualFirstTime";
    public static final String FIELD_ACTUAL_LAST_TIME = "actualLastTime";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_TRX_ASSEMBLE_QTY = "trxAssembleQty";
    public static final String FIELD_TRX_SCRAPPED_QTY = "trxScrappedQty";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 4704347118131249705L;

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
    @ApiModelProperty("生产订单组件实绩历史ID")
    @Id
    @Where
    private String workOrderCompActualHisId;
    @ApiModelProperty(value = "生产订单组件实绩ID", required = true)
    @NotBlank
    @Where
    private String workOrderComponentActualId;
    @ApiModelProperty(value = "WO主键ID，标识实绩对应的唯一生产订单", required = true)
    @NotBlank
    @Where
    private String workOrderId;
    @ApiModelProperty(value = "实际装配物料ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "实际装配OPERATION")
    @Where
    private String operationId;
    @ApiModelProperty(value = "实际装配数量", required = true)
    @NotNull
    @Where
    private Double assembleQty;
    @ApiModelProperty(value = "实际报废数量", required = true)
    @NotNull
    @Where
    private Double scrappedQty;
    @ApiModelProperty(value = "组件类型，如装配、拆卸、联产品等", required = true)
    @NotBlank
    @Where
    private String componentType;
    @ApiModelProperty(value = "非强制装配时物料清单行ID")
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "装配时引用的装配清单")
    @Where
    private String bomId;
    @ApiModelProperty(value = "非强制装配时组件所属装配步骤")
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "强制装配标识，“Y”代表该记录为强制装配，不属于原装配清单部分")
    @Where
    private String assembleExcessFlag;
    @ApiModelProperty(value = "是否为NC或特殊工艺路线装配，均属于强制装配")
    @Where
    private String assembleRouterType;
    @ApiModelProperty(value = "替代装配标识，“Y”代表装配的是替代件，如果是替代，则不属于强制装配")
    @Where
    private String substituteFlag;
    @ApiModelProperty(value = "第一次装配时间")
    @Where
    private Date actualFirstTime;
    @ApiModelProperty(value = "最近一次装配时间，最终体现确认时间")
    @Where
    private Date actualLastTime;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "事务数量-装配数量", required = true)
    @NotNull
    @Where
    private Double trxAssembleQty;
    @ApiModelProperty(value = "事务数量-报废数量", required = true)
    @NotNull
    @Where
    private Double trxScrappedQty;
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
     * @return 生产订单组件实绩历史ID
     */
    public String getWorkOrderCompActualHisId() {
        return workOrderCompActualHisId;
    }

    public void setWorkOrderCompActualHisId(String workOrderCompActualHisId) {
        this.workOrderCompActualHisId = workOrderCompActualHisId;
    }

    /**
     * @return 生产订单组件实绩ID
     */
    public String getWorkOrderComponentActualId() {
        return workOrderComponentActualId;
    }

    public void setWorkOrderComponentActualId(String workOrderComponentActualId) {
        this.workOrderComponentActualId = workOrderComponentActualId;
    }

    /**
     * @return WO主键ID，标识实绩对应的唯一生产订单
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
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
     * @return 实际装配OPERATION
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 实际装配数量
     */
    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    /**
     * @return 实际报废数量
     */
    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    /**
     * @return 组件类型，如装配、拆卸、联产品等
     */
    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * @return 非强制装配时物料清单行ID
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 装配时引用的装配清单
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    /**
     * @return 非强制装配时组件所属装配步骤
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
     * @return 是否为NC或特殊工艺路线装配，均属于强制装配
     */
    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    /**
     * @return 替代装配标识，“Y”代表装配的是替代件，如果是替代，则不属于强制装配
     */
    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    /**
     * @return 第一次装配时间
     */
    public Date getActualFirstTime() {
        if (actualFirstTime != null) {
            return (Date) actualFirstTime.clone();
        } else {
            return null;
        }
    }

    public void setActualFirstTime(Date actualFirstTime) {
        if (actualFirstTime == null) {
            this.actualFirstTime = null;
        } else {
            this.actualFirstTime = (Date) actualFirstTime.clone();
        }
    }

    public Date getActualLastTime() {
        if (actualLastTime != null) {
            return (Date) actualLastTime.clone();
        } else {
            return null;
        }
    }

    public void setActualLastTime(Date actualLastTime) {
        if (actualLastTime == null) {
            this.actualLastTime = null;
        } else {
            this.actualLastTime = (Date) actualLastTime.clone();
        }
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
     * @return 事务数量-装配数量
     */
    public Double getTrxAssembleQty() {
        return trxAssembleQty;
    }

    public void setTrxAssembleQty(Double trxAssembleQty) {
        this.trxAssembleQty = trxAssembleQty;
    }

    /**
     * @return 事务数量-报废数量
     */
    public Double getTrxScrappedQty() {
        return trxScrappedQty;
    }

    public void setTrxScrappedQty(Double trxScrappedQty) {
        this.trxScrappedQty = trxScrappedQty;
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