package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class MtAssembleProcessActualVO16 implements Serializable {

    private static final long serialVersionUID = 4724086317096835778L;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("装配操作人")
    private String operationBy;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("父事件ID")
    private String parentEventId;
    @ApiModelProperty("事件组ID")
    private String eventRequestId;
    @ApiModelProperty("日历日期")
    private LocalDate shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("库位ID")
    private String locatorId;

    @ApiModelProperty("eo信息")
    private List<MtAssembleProcessActualVO17> eoInfo;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationBy() {
        return operationBy;
    }

    public void setOperationBy(String operationBy) {
        this.operationBy = operationBy;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public List<MtAssembleProcessActualVO17> getEoInfo() {
        return eoInfo;
    }

    public void setEoInfo(List<MtAssembleProcessActualVO17> eoInfo) {
        this.eoInfo = eoInfo;
    }
}


