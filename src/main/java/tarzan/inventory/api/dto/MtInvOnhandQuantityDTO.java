package tarzan.inventory.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import tarzan.modeling.domain.vo.MtModOrganizationVO3;

/**
 * @author benjamin
 */
public class MtInvOnhandQuantityDTO implements Serializable {
    private static final long serialVersionUID = 7704620955001136026L;

    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "组织类型")
    private String orgType;
    @ApiModelProperty(value = "组织Id")
    private String orgId;
    @ApiModelProperty(value = "站点Id")
    private String siteId;
    @ApiModelProperty(value = "所有者类型")
    private String ownerType;
    @ApiModelProperty(value = "所有者Id")
    private String ownerId;
    @ApiModelProperty(value = "预留标识")
    private String holdFlag;
    @ApiModelProperty(value = "预留类型")
    private String holdType;
    @ApiModelProperty(value = "预留数量")
    private Double holdQuantity;
    @ApiModelProperty(value = "预留指令类型")
    private String orderType;
    @ApiModelProperty(value = "预留指令")
    private String orderId;
    @ApiModelProperty(value = "批次集合")
    private List<String> lotCode;
    @ApiModelProperty(value = "组织集合")
    private List<MtModOrganizationVO3> orgList;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public String getHoldFlag() {
        return holdFlag;
    }

    public void setHoldFlag(String holdFlag) {
        this.holdFlag = holdFlag;
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public Double getHoldQuantity() {
        return holdQuantity;
    }

    public void setHoldQuantity(Double holdQuantity) {
        this.holdQuantity = holdQuantity;
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

    public List<String> getLotCode() {
        return lotCode;
    }

    public void setLotCode(List<String> lotCode) {
        this.lotCode = lotCode;
    }

    public List<MtModOrganizationVO3> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<MtModOrganizationVO3> orgList) {
        this.orgList = orgList;
    }
}
