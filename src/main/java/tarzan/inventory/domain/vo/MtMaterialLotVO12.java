package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtMaterialLotVO12 implements Serializable {

    private static final long serialVersionUID = 4920975171453497872L;

    private String materialLotId;// 物料批
    private String targetOwnerType;// 目标站点
    private String targetOwnerId;// 目标库位
    private String workcellId;// 工作单元
    private String parentEventId;// 父事件ID
    private String eventRequestId;// 事件组ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;// 日历日期
    private String shiftCode;// 班次编码

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getTargetOwnerType() {
        return targetOwnerType;
    }

    public void setTargetOwnerType(String targetOwnerType) {
        this.targetOwnerType = targetOwnerType;
    }

    public String getTargetOwnerId() {
        return targetOwnerId;
    }

    public void setTargetOwnerId(String targetOwnerId) {
        this.targetOwnerId = targetOwnerId;
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
}
