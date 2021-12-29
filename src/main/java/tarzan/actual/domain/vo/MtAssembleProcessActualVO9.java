package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2020-10-30 15:45
 **/
public class MtAssembleProcessActualVO9 implements Serializable {
    private static final long serialVersionUID = 1759896469366721263L;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("装配操作人")
    private String operationBy;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("不需要校验组件装配类型")
    private String checkComponentTypeFlag;


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


    @ApiModelProperty("EO信息")
    private List<MtAssembleProcessActualVO10> eoInfo;

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

    public String getCheckComponentTypeFlag() {
        return checkComponentTypeFlag;
    }

    public void setCheckComponentTypeFlag(String checkComponentTypeFlag) {
        this.checkComponentTypeFlag = checkComponentTypeFlag;
    }

    public List<MtAssembleProcessActualVO10> getEoInfo() {
        return eoInfo;
    }

    public void setEoInfo(List<MtAssembleProcessActualVO10> eoInfo) {
        this.eoInfo = eoInfo;
    }

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
}
