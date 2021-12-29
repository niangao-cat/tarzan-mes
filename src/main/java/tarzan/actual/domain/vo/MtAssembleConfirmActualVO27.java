package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author MrZ
 */
public class MtAssembleConfirmActualVO27 implements Serializable {


    private static final long serialVersionUID = -7836233649815251485L;
    @ApiModelProperty("工作单元Id")
    private String workcellId;
    @ApiModelProperty("参考区域")
    private String referenceArea;
    @ApiModelProperty("装配操作人")
    private Long operationBy;
    @ApiModelProperty("父事件ID")
    private String parentEventId;
    @ApiModelProperty("事件组ID")
    private String eventRequestId;
    @ApiModelProperty("日历日期")
    private LocalDate shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;

    @ApiModelProperty("装配组工作单元Id")
    private String agWorkcellId;

    @ApiModelProperty("装配组工作单元Id")
    private List<MtAssembleConfirmActualVO28> eoInfo;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getReferenceArea() {
        return referenceArea;
    }

    public void setReferenceArea(String referenceArea) {
        this.referenceArea = referenceArea;
    }

    public Long getOperationBy() {
        return operationBy;
    }

    public void setOperationBy(Long operationBy) {
        this.operationBy = operationBy;
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

    public String getAgWorkcellId() {
        return agWorkcellId;
    }

    public void setAgWorkcellId(String agWorkcellId) {
        this.agWorkcellId = agWorkcellId;
    }

    public List<MtAssembleConfirmActualVO28> getEoInfo() {
        return eoInfo;
    }

    public void setEoInfo(List<MtAssembleConfirmActualVO28> eoInfo) {
        this.eoInfo = eoInfo;
    }
}


