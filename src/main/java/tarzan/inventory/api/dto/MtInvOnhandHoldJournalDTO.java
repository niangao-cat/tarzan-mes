package tarzan.inventory.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtInvOnhandHoldJournalDTO implements Serializable {
    private static final long serialVersionUID = -2345165311707363073L;

    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endTime;
    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "批次编码")
    private String lotCode;
    @ApiModelProperty(value = "组织类型")
    private String orgType;
    @ApiModelProperty(value = "组织Id")
    private String orgId;
    @ApiModelProperty(value = "所有者类型")
    private String ownerType;
    @ApiModelProperty(value = "所有者Id")
    private String ownerId;
    @ApiModelProperty(value = "预留类型")
    private String holdType;
    @ApiModelProperty(value = "预留指令类型")
    private String orderType;
    @ApiModelProperty(value = "预留指令编码")
    private String orderId;

    public Date getStartTime() {
        if (startTime == null) {
            return null;
        } else {
            return (Date) startTime.clone();
        }
    }

    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = (Date) startTime.clone();
        }
    }

    public Date getEndTime() {
        if (endTime == null) {
            return null;
        } else {
            return (Date) endTime.clone();
        }
    }

    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = null;
        } else {
            this.endTime = (Date) endTime.clone();
        }
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
