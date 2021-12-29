package tarzan.actual.domain.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 装配过程实绩，记录每一次执行作业的材料明细装配记录
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("装配过程实绩，记录每一次执行作业的材料明细装配记录")

@ModifyAudit

@Table(name = "mt_assemble_process_actual")
@CustomPrimary
public class MtAssembleProcessActual extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_PROCESS_ACTUAL_ID = "assembleProcessActualId";
    public static final String FIELD_ASSEMBLE_CONFIRM_ACTUAL_ID = "assembleConfirmActualId";
    public static final String FIELD_ASSEMBLE_QTY = "assembleQty";
    public static final String FIELD_SCRAP_QTY = "scrapQty";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_SUBSTEP_ID = "substepId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_ASSEMBLE_GROUP_ID = "assembleGroupId";
    public static final String FIELD_ASSEMBLE_POINT_ID = "assemblePointId";
    public static final String FIELD_REFERENCE_AREA = "referenceArea";
    public static final String FIELD_REFERENCE_POINT = "referencePoint";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_ASSEMBLE_METHOD = "assembleMethod";
    public static final String FIELD_OPERATE_BY = "operateBy";
    public static final String FIELD_MATERIAL_LOT_HIS_ID = "materialLotHisId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_EVENT_TIME = "eventTime";
    public static final String FIELD_EVENT_BY = "eventBy";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -4831007267407392116L;

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
    @ApiModelProperty("装配过程实绩")
    @Id
    @Where
    private String assembleProcessActualId;
    @ApiModelProperty(value = "装配确认实绩", required = true)
    @NotBlank
    @Where
    private String assembleConfirmActualId;
    @ApiModelProperty(value = "本次装配数量，六位小数", required = true)
    @NotNull
    @Where
    private Double assembleQty;
    @ApiModelProperty(value = "本次报废数量，六位小数", required = true)
    @NotNull
    @Where
    private Double scrapQty;
    @ApiModelProperty(value = "实际装配所在工艺路线")
    @Where
    private String routerId;
    @ApiModelProperty(value = "子步骤")
    @Where
    private String substepId;
    @ApiModelProperty(value = "物料批")
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "工作单元")
    @Where
    private String workcellId;
    @ApiModelProperty(value = "装配组")
    @Where
    private String assembleGroupId;
    @ApiModelProperty(value = "装配点")
    @Where
    private String assemblePointId;
    @ApiModelProperty(value = "参考区域")
    @Where
    private String referenceArea;
    @ApiModelProperty(value = "参考点")
    @Where
    private String referencePoint;
    @ApiModelProperty(value = "装配库位")
    @Where
    private String locatorId;
    @ApiModelProperty(value = "装配方式(投料/上料位反冲/库存反冲)")
    @Where
    private String assembleMethod;
    @ApiModelProperty(value = "操作人", required = true)
    @NotNull
    @Where
    private Long operateBy;
    @ApiModelProperty(value = "物料批历史ID")
    @Where
    private String materialLotHisId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "事件时间", required = true)
    @NotNull
    @Where
    private Date eventTime;
    @ApiModelProperty(value = "事件人", required = true)
    @NotNull
    @Where
    private Long eventBy;
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
     * @return 装配过程实绩
     */
    public String getAssembleProcessActualId() {
        return assembleProcessActualId;
    }

    public void setAssembleProcessActualId(String assembleProcessActualId) {
        this.assembleProcessActualId = assembleProcessActualId;
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
     * @return 本次装配数量，六位小数
     */
    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    /**
     * @return 本次报废数量，六位小数
     */
    public Double getScrapQty() {
        return scrapQty;
    }

    public void setScrapQty(Double scrapQty) {
        this.scrapQty = scrapQty;
    }

    /**
     * @return 实际装配所在工艺路线
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 子步骤
     */
    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    /**
     * @return 物料批
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 装配组
     */
    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    /**
     * @return 装配点
     */
    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    /**
     * @return 参考区域
     */
    public String getReferenceArea() {
        return referenceArea;
    }

    public void setReferenceArea(String referenceArea) {
        this.referenceArea = referenceArea;
    }

    /**
     * @return 参考点
     */
    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    /**
     * @return 装配库位
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 装配方式(投料/上料位反冲/库存反冲)
     */
    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    /**
     * @return 操作人
     */
    public Long getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(Long operateBy) {
        this.operateBy = operateBy;
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
     * @return 事件时间
     */

    public Date getEventTime() {
        if (eventTime != null) {
            return (Date) eventTime.clone();
        } else {
            return null;
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }

    /**
     * @return 事件人
     */
    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
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

    /**
     *  @return 物料批历史ID
     */
    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }
}
