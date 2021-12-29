package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/3/22 9:46
 */
public class MtAssembleProcessActualVO implements Serializable {
    private static final long serialVersionUID = -7979636394975145867L;

    @ApiModelProperty("装配过程实绩ID")
    private String assembleProcessActualId;
    @ApiModelProperty("装配确认实绩ID")
    private String assembleConfirmActualId;
    @ApiModelProperty("装配时工艺路线ID")
    private String routerId;
    @ApiModelProperty("装配时子步骤ID")
    private String substepId;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("装配组ID")
    private String assembleGroupId;
    @ApiModelProperty("装配点ID")
    private String assemblePointId;
    @ApiModelProperty("参考区域")
    private String referenceArea;
    @ApiModelProperty("参考点")
    private String referencePoint;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("装配方式")
    private String assembleMethod;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("事件人")
    private String eventBy;
    @ApiModelProperty("操作人")
    private String operateBy;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("事件时间从")
    private Date eventTimeFrom;
    @ApiModelProperty("事件时间到")
    private Date eventTimeTo;
    @ApiModelProperty("物料批历史ID")
    private String materialLotHisId;


    public String getAssembleProcessActualId() {
        return assembleProcessActualId;
    }

    public void setAssembleProcessActualId(String assembleProcessActualId) {
        this.assembleProcessActualId = assembleProcessActualId;
    }

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

    public String getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(String operateBy) {
        this.operateBy = operateBy;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public Date getEventTimeFrom() {
        if (eventTimeFrom == null) {
            return null;
        } else {
            return (Date) eventTimeFrom.clone();
        }
    }

    public void setEventTimeFrom(Date eventTimeFrom) {

        if (eventTimeFrom == null) {
            this.eventTimeFrom = null;
        } else {
            this.eventTimeFrom = (Date) eventTimeFrom.clone();
        }
    }

    public Date getEventTimeTo() {
        if (eventTimeTo == null) {
            return null;
        } else {
            return (Date) eventTimeTo.clone();
        }
    }

    public void setEventTimeTo(Date eventTimeTo) {

        if (eventTimeTo == null) {
            this.eventTimeTo = null;
        } else {
            this.eventTimeTo = (Date) eventTimeTo.clone();
        }
    }

    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }
}
