package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/4/4 14:28
 */
public class MtContainerVO7 implements Serializable {
    private static final long serialVersionUID = -1425464829079812220L;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("目标站点")
    private String targetSiteId;
    @ApiModelProperty("目标库位")
    private String targetLocatorId;
    @ApiModelProperty("工作单元")
    private String workcellId;
    @ApiModelProperty("父事件ID")
    private String parentEventId;
    @ApiModelProperty("事件组ID")
    private String eventRequestId;
    @ApiModelProperty("日历日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("指令单据ID")
    private String instructionDocId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getTargetSiteId() {
        return targetSiteId;
    }

    public void setTargetSiteId(String targetSiteId) {
        this.targetSiteId = targetSiteId;
    }

    public String getTargetLocatorId() {
        return targetLocatorId;
    }

    public void setTargetLocatorId(String targetLocatorId) {
        this.targetLocatorId = targetLocatorId;
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

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }
}
