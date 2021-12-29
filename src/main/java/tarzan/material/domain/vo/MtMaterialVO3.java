package tarzan.material.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;


public class MtMaterialVO3 implements Serializable {

    private static final long serialVersionUID = -2968194459584719725L;

    @ApiModelProperty("来源物料批ID")
    private String sourceMaterialLotId;
    @ApiModelProperty("拆分主单位数量")
    private Double splitPrimaryQty;
    @ApiModelProperty("拆分辅助单位数量")
    private Double splitSecondaryQty;
    @ApiModelProperty("容器行")
    private Long locationRow;
    @ApiModelProperty("容器列")
    private Long locationColumn;
    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("传入值参数列表")
    private List<String> incomingValueList;
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
    @ApiModelProperty("外部输入物料批编码")
    private String splitMaterialLotCode;

    public String getSourceMaterialLotId() {
        return sourceMaterialLotId;
    }

    public void setSourceMaterialLotId(String sourceMaterialLotId) {
        this.sourceMaterialLotId = sourceMaterialLotId;
    }

    public Double getSplitPrimaryQty() {
        return splitPrimaryQty;
    }

    public void setSplitPrimaryQty(Double splitPrimaryQty) {
        this.splitPrimaryQty = splitPrimaryQty;
    }

    public Double getSplitSecondaryQty() {
        return splitSecondaryQty;
    }

    public void setSplitSecondaryQty(Double splitSecondaryQty) {
        this.splitSecondaryQty = splitSecondaryQty;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
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

    public String getSplitMaterialLotCode() {
        return splitMaterialLotCode;
    }

    public void setSplitMaterialLotCode(String splitMaterialLotCode) {
        this.splitMaterialLotCode = splitMaterialLotCode;
    }
}
