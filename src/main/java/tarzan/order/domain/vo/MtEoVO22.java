package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/3/19 16:58
 */
public class MtEoVO22 implements Serializable {
    private static final long serialVersionUID = 304084822095422175L;

    @ApiModelProperty("主合并来源执行作业")
    private String primaryEoId;
    @ApiModelProperty("当前执行作业步骤实际")
    private String sourceEoStepActualId;
    @ApiModelProperty("副合并来源执行作业列表")
    private List<MtEoVO41> secondaryEoIds;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历日期")
    private Date shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;

    public String getPrimaryEoId() {
        return primaryEoId;
    }

    public void setPrimaryEoId(String primaryEoId) {
        this.primaryEoId = primaryEoId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public List<MtEoVO41> getSecondaryEoIds() {
        return secondaryEoIds;
    }

    public void setSecondaryEoIds(List<MtEoVO41> secondaryEoIds) {
        this.secondaryEoIds = secondaryEoIds;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
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
