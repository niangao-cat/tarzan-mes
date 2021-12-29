package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class MtAssemblePointDTO4 implements Serializable {

    private static final long serialVersionUID = 7275925859093710456L;
    @ApiModelProperty(value = "装配点")
    private String assemblePointId;
    @ApiModelProperty(value = "装配物料")
    private String materialId;
    @ApiModelProperty(value = "装配物料批")
    private String materialLotId;
    @ApiModelProperty(value = "卸载数量")
    private Double qty;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;
    @ApiModelProperty(value = "父事件ID")
    private String parentEventId;
    @ApiModelProperty(value = "事件组ID")
    private String eventRequestId;
    @ApiModelProperty(value = "日历日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
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
        builder.append("MtAssemblePointDTO4 [assemblePointId=");
        builder.append(assemblePointId);
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
