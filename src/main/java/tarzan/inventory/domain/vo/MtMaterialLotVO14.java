package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/10/10 17:34
 * @Description:
 */
public class MtMaterialLotVO14 implements Serializable {

    private static final long serialVersionUID = 1575726545782345757L;
    @ApiModelProperty("物料批")
    private List<String> materialLotIds;
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

    public List<String> getMaterialLotIds() {
        return materialLotIds;
    }

    public void setMaterialLotIds(List<String> materialLotIds) {
        this.materialLotIds = materialLotIds;
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

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }
}
