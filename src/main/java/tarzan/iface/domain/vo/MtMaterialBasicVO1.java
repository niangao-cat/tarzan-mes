package tarzan.iface.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtMaterialBasicVO1 implements Serializable {

    private static final long serialVersionUID = 2383475808615296290L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialSiteId;
    @ApiModelProperty("物料名称")
    private String oldItemCode;
    @ApiModelProperty("物料图号")
    private String longDescription;
    @ApiModelProperty("物料简码")
    private String itemType;
    @ApiModelProperty("材质&型号")
    private String makeBuyCode;
    @ApiModelProperty("基本计量单位")
    private String lotControlCode;
    @ApiModelProperty("辅助计量单位")
    private String qcFlag;
    @ApiModelProperty("是否有效")
    private String receivingRoutingId;
    @ApiModelProperty("物料ID")
    private String wipSupplyType;
    @ApiModelProperty("物料站点ID")
    private String vmiFlag;
    @ApiModelProperty("旧物料编码")
    private String itemGroup;
    @ApiModelProperty("物料长描述")
    private String productGroup;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    public String getOldItemCode() {
        return oldItemCode;
    }

    public void setOldItemCode(String oldItemCode) {
        this.oldItemCode = oldItemCode;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getMakeBuyCode() {
        return makeBuyCode;
    }

    public void setMakeBuyCode(String makeBuyCode) {
        this.makeBuyCode = makeBuyCode;
    }

    public String getLotControlCode() {
        return lotControlCode;
    }

    public void setLotControlCode(String lotControlCode) {
        this.lotControlCode = lotControlCode;
    }

    public String getQcFlag() {
        return qcFlag;
    }

    public void setQcFlag(String qcFlag) {
        this.qcFlag = qcFlag;
    }

    public String getReceivingRoutingId() {
        return receivingRoutingId;
    }

    public void setReceivingRoutingId(String receivingRoutingId) {
        this.receivingRoutingId = receivingRoutingId;
    }

    public String getWipSupplyType() {
        return wipSupplyType;
    }

    public void setWipSupplyType(String wipSupplyType) {
        this.wipSupplyType = wipSupplyType;
    }

    public String getVmiFlag() {
        return vmiFlag;
    }

    public void setVmiFlag(String vmiFlag) {
        this.vmiFlag = vmiFlag;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

}
