package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author: changbu  2020/11/2 9:44
 */
public class MtAssembleProcessActualVO18 implements Serializable {

    private static final long serialVersionUID = -5229177961295866539L;

    @ApiModelProperty("工序")
    private String operationId;

    @ApiModelProperty("装配操作人")
    private String operationBy;

    @ApiModelProperty("组件类型标识")
    private String checkComponentTypeFlag;

    @ApiModelProperty("事件")
    private String eventId;

    @ApiModelProperty("工作单元")
    private String workcellId;

    @ApiModelProperty("货位")
    private String locatorId;

    @ApiModelProperty("父事件")
    private String parentEventId;

    @ApiModelProperty("事件请求")
    private String eventRequestId;

    @ApiModelProperty("班次日历")
    private LocalDate shiftDate;

    @ApiModelProperty("班次编码")
    private String shiftCode;

    @ApiModelProperty("执行作业列表")
    private List<MtAssembleProcessActualVO14> woInfo;

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

    public String getCheckComponentTypeFlag() {
        return checkComponentTypeFlag;
    }

    public void setCheckComponentTypeFlag(String checkComponentTypeFlag) {
        this.checkComponentTypeFlag = checkComponentTypeFlag;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
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

    public List<MtAssembleProcessActualVO14> getWoInfo() {
        return woInfo;
    }

    public void setWoInfo(List<MtAssembleProcessActualVO14> woInfo) {
        this.woInfo = woInfo;
    }
}
