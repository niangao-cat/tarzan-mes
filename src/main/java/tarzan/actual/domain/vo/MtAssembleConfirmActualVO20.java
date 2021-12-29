package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;


public class MtAssembleConfirmActualVO20 implements Serializable {

    private static final long serialVersionUID = 7069283030643987597L;

    @ApiModelProperty(value = "主键", required = true)
    private String assembleConfirmActualId;
    @ApiModelProperty(value = "EO主键ID，标识实绩对应的唯一执行作业", required = true)
    private String eoId;
    @ApiModelProperty(value = "实际装配物料ID", required = true)
    private String materialId;
    @ApiModelProperty(value = "实际装配OPERATION，实绩装配工艺与需求步骤工艺不一致时也判断为强制装配")
    private String operationId;
    @ApiModelProperty(value = "实际装配组件类型，如装配、拆卸、联产品等", required = true)
    private String componentType;
    @ApiModelProperty(value = "非强制装配时物料对应装配清单行ID")
    private String bomComponentId;
    @ApiModelProperty(value = "装配时执行作业引用的装配清单")
    private String bomId;
    @ApiModelProperty(value = "非强制装配时物料对应组件装配步骤需求")
    private String routerStepId;
    @ApiModelProperty(value = "强制装配标识，“Y”代表该记录为强制装配，不属于原装配清单部分")
    private String assembleExcessFlag;
    @ApiModelProperty(value = "包括NC和特殊工艺路线装配，均属于强制装配")
    private String assembleRouterType;
    @ApiModelProperty(value = "替代装配标识，“Y”代表装配的是替代件，如果是替代")
    private String substituteFlag;
    @ApiModelProperty(value = "装配遗留标识，“Y”代表用户主动遗留")
    private String bypassFlag;
    @ApiModelProperty(value = "遗留人")
    private String bypassBy;
    @ApiModelProperty(value = "装配确认标识，“Y”代表该行记录以被确认")
    private String confirmFlag;
    @ApiModelProperty(value = "确认人")
    private String confirmedBy;
    @ApiModelProperty("装配过程实绩")
    private String assembleProcessActualId;
    @ApiModelProperty(value = "本次装配数量，六位小数", required = true)
    private Double assembleQty;
    @ApiModelProperty(value = "本次报废数量，六位小数", required = true)
    private Double scrapQty;
    @ApiModelProperty(value = "实际装配所在工艺路线")
    private String routerId;
    @ApiModelProperty(value = "子步骤")
    private String substepId;
    @ApiModelProperty(value = "物料批")
    private String materialLotId;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;
    @ApiModelProperty(value = "装配组")
    private String assembleGroupId;
    @ApiModelProperty(value = "装配点")
    private String assemblePointId;
    @ApiModelProperty(value = "参考区域")
    private String referenceArea;
    @ApiModelProperty(value = "参考点")
    private String referencePoint;
    @ApiModelProperty(value = "装配库位")
    private String locatorId;
    @ApiModelProperty(value = "装配方式(投料/上料位反冲/库存反冲)")
    private String assembleMethod;
    @ApiModelProperty(value = "操作人", required = true)
    private Long operateBy;
    @ApiModelProperty(value = "物料批历史ID")
    private String materialLotHisId;
    @ApiModelProperty(value = "事件ID", required = true)
    private String eventId;
    @ApiModelProperty(value = "事件时间", required = true)
    private Date eventTime;
    @ApiModelProperty(value = "事件人", required = true)
    private Long eventBy;

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getBypassFlag() {
        return bypassFlag;
    }

    public void setBypassFlag(String bypassFlag) {
        this.bypassFlag = bypassFlag;
    }

    public String getBypassBy() {
        return bypassBy;
    }

    public void setBypassBy(String bypassBy) {
        this.bypassBy = bypassBy;
    }

    public String getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public String getAssembleProcessActualId() {
        return assembleProcessActualId;
    }

    public void setAssembleProcessActualId(String assembleProcessActualId) {
        this.assembleProcessActualId = assembleProcessActualId;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Double getScrapQty() {
        return scrapQty;
    }

    public void setScrapQty(Double scrapQty) {
        this.scrapQty = scrapQty;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getReferenceArea() {
        return referenceArea;
    }

    public void setReferenceArea(String referenceArea) {
        this.referenceArea = referenceArea;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public Long getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(Long operateBy) {
        this.operateBy = operateBy;
    }

    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Date getEventTime() {
        return eventTime == null ? null : (Date) eventTime.clone();
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime == null ? null : (Date) eventTime.clone();
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

}
