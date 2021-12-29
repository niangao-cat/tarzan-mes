package tarzan.general.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MtEventDTO3 implements Serializable {
    private static final long serialVersionUID = -7051123947267956779L;
    private Boolean eventFlag;
    private String eventRequestId;
    private String kid;
    private String typeCode;
    private String typeDesc;
    private Date operationTime;
    private Long operationBy;
    private String operationUserName;
    private String workcellId;
    private String workcellCode;
    private String locatorId;
    private String locatorCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;
    private String parentEventId;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    public Boolean getEventFlag() {
        return eventFlag;
    }

    public void setEventFlag(Boolean eventFlag) {
        this.eventFlag = eventFlag;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public Date getOperationTime() {
        if (operationTime != null) {
            return (Date) operationTime.clone();
        } else {
            return null;
        }
    }

    public void setOperationTime(Date operationTime) {
        if (operationTime == null) {
            this.operationTime = null;
        } else {
            this.operationTime = (Date) operationTime.clone();
        }
    }

    public Long getOperationBy() {
        return operationBy;
    }

    public void setOperationBy(Long operationBy) {
        this.operationBy = operationBy;
    }

    public String getOperationUserName() {
        return operationUserName;
    }

    public void setOperationUserName(String operationUserName) {
        this.operationUserName = operationUserName;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
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

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public BigDecimal getPrimaryUomQty() {
        return primaryUomQty;
    }

    public void setPrimaryUomQty(BigDecimal primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }
}
