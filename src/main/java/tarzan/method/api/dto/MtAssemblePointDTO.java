package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtAssemblePointDTO implements Serializable {

    private static final long serialVersionUID = 2931937162210006853L;
    private String assembleGroupId;// 装配组
    private String materialId;// 装配物料
    private String materialLotId;// 装配物料批
    private Double qty;// 装配数量
    private String workcellId;// 工作单元ID
    private String parentEventId;// 父事件ID
    private String eventRequestId;// 事件组ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;// 日历日期
    private String shiftCode;// 班次编码

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtAssemblePointControlDTO2 [assembleGroupId=");
        builder.append(assembleGroupId);
        builder.append(", materialId=");
        builder.append(materialId);
        builder.append(", materialLotId=");
        builder.append(materialLotId);
        builder.append(", qty=");
        builder.append(qty);
        builder.append(", workcellId=");
        builder.append(workcellId);
        builder.append(", parentEventId=");
        builder.append(parentEventId);
        builder.append(", eventRequestId=");
        builder.append(eventRequestId);
        builder.append(", shiftDate=");
        builder.append(shiftDate);
        builder.append(", shiftCode=");
        builder.append(shiftCode);
        builder.append("]");
        return builder.toString();
    }

}
