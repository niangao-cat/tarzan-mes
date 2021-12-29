package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/3/20 19:41
 */
public class MtEoVO24 implements Serializable {
    private static final long serialVersionUID = 7098282215786617895L;
    @ApiModelProperty("当前执行作业")
    private String sourceEoId;
    @ApiModelProperty("拆分数量")
    private Double splitQty;
    @ApiModelProperty("当前执行作业步骤实际")
    private String sourceEoStepActualId;
    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("传入值参数列表")
    private List<String> incomingValueList;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("父事件ID")
    private String parentEventId;
    @ApiModelProperty("事件组ID")
    private String eventRequestId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历日期")
    private Date shiftDate;

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public Double getSplitQty() {
        return splitQty;
    }

    public void setSplitQty(Double splitQty) {
        this.splitQty = splitQty;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        } else {
            return (Date) shiftDate.clone();
        }
    }

    public void setShiftDate(Date shiftDate) {

        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
