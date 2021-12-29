package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/22 10:37
 * @Author: ${yiyang.xie}
 */
public class MtEoVO34 implements Serializable {
    private static final long serialVersionUID = -112596591771192512L;

    @ApiModelProperty("来源执行作业ID")
    private String sourceEoId;

    @ApiModelProperty("指定拆分目标执行作业列表")
    private List<MtEoVO35> targetEoList;

    @ApiModelProperty("工作单元ID")
    private String workcellId;

    @ApiModelProperty("库位ID")
    private String locatorId;

    @ApiModelProperty("父事件ID")
    private String parentEventId;

    @ApiModelProperty("事件组ID")
    private String eventRequestId;

    @ApiModelProperty("班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

    @ApiModelProperty("班次编码")
    private String shiftCode;


    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public List<MtEoVO35> getTargetEoList() {
        return targetEoList;
    }

    public void setTargetEoList(List<MtEoVO35> targetEoList) {
        this.targetEoList = targetEoList;
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
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
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
}
