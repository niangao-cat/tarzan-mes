package tarzan.iface.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtMaterialBasicVO2 implements Serializable {

    private static final long serialVersionUID = -1438659581768329438L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料图号")
    private String materialSiteId;
    @ApiModelProperty("物料简码")
    private String oldItemCode;
    @ApiModelProperty("材质&型号")
    private String longDescription;
    @ApiModelProperty("物料长度值")
    private String itemType;
    @ApiModelProperty("物料宽度值")
    private String makeBuyCode;
    @ApiModelProperty("物料高度值")
    private String lotControlCode;
    @ApiModelProperty("尺寸单位ID")
    private String qcFlag;
    @ApiModelProperty("尺寸单位编码")
    private String receivingRoutingId;
    @ApiModelProperty("尺寸单位名称")
    private String wipSupplyType;
    @ApiModelProperty("物料体积")
    private String vmiFlag;
    @ApiModelProperty("体积单位ID")
    private String itemGroup;
    @ApiModelProperty("体积单位编码")
    private String productGroup;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
