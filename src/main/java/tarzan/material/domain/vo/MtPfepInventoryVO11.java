package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2020-09-28 14:57
 **/
public class MtPfepInventoryVO11 implements Serializable {
    private static final long serialVersionUID = 2331001635317884545L;
    @ApiModelProperty(value = "存储标识类型，如无标识、容器标识、物料批标识等")
    private String identifyType;
    @ApiModelProperty(value = "标识模板，关联物料默认使用的标识模板")
    private String identifyId;
    @ApiModelProperty(value = "默认存储库位")
    private String stockLocatorId;
    @ApiModelProperty(value = "存储包装长")
    private Double packageLength;
    @ApiModelProperty(value = "存储包装宽")
    private Double packageWidth;
    @ApiModelProperty(value = "存储包装高")
    private Double packageHeight;
    @ApiModelProperty(value = "包装尺寸单位，如MMCMM等，与单位维护保持一致")
    private String packageSizeUomId;
    @ApiModelProperty(value = "存储包装重量")
    private Double packageWeight;
    @ApiModelProperty(value = "重量单位，如KG/G/T等")
    private String weightUomId;
    @ApiModelProperty(value = "最大存储库存")
    private Double maxStockQty;
    @ApiModelProperty(value = "最小存储库存")
    private Double minStockQty;
    @ApiModelProperty(value = "默认发料库位")
    private String issuedLocatorId;
    @ApiModelProperty(value = "默认完工库位")
    private String completionLocatorId;

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    public String getStockLocatorId() {
        return stockLocatorId;
    }

    public void setStockLocatorId(String stockLocatorId) {
        this.stockLocatorId = stockLocatorId;
    }

    public Double getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(Double packageLength) {
        this.packageLength = packageLength;
    }

    public Double getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(Double packageWidth) {
        this.packageWidth = packageWidth;
    }

    public Double getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(Double packageHeight) {
        this.packageHeight = packageHeight;
    }

    public String getPackageSizeUomId() {
        return packageSizeUomId;
    }

    public void setPackageSizeUomId(String packageSizeUomId) {
        this.packageSizeUomId = packageSizeUomId;
    }

    public Double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    public Double getMaxStockQty() {
        return maxStockQty;
    }

    public void setMaxStockQty(Double maxStockQty) {
        this.maxStockQty = maxStockQty;
    }

    public Double getMinStockQty() {
        return minStockQty;
    }

    public void setMinStockQty(Double minStockQty) {
        this.minStockQty = minStockQty;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
    }
}
