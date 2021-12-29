package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * 配送数量计算
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 16:24
 */
public class WmsDistributionQtyVO {
    @ApiModelProperty(value = "组件物料ID")
    private String materialId;
    @ApiModelProperty(value = "组件物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "工段（工作单元）ID")
    private String workcellId;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    public String getMaterialId() {
        return materialId;
    }

    public WmsDistributionQtyVO setMaterialId(String materialId) {
        this.materialId = materialId;
        return this;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public WmsDistributionQtyVO setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
        return this;
    }

    public String getMaterialVersion() {
        return materialVersion;
    }

    public WmsDistributionQtyVO setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
        return this;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public WmsDistributionQtyVO setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
        return this;
    }

    public String getSoNum() {
        return soNum;
    }

    public WmsDistributionQtyVO setSoNum(String soNum) {
        this.soNum = soNum;
        return this;
    }

    public String getSoLineNum() {
        return soLineNum;
    }

    public WmsDistributionQtyVO setSoLineNum(String soLineNum) {
        this.soLineNum = soLineNum;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public WmsDistributionQtyVO setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "WmsDistributionQtyVO{" +
                "materialId='" + materialId + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialVersion='" + materialVersion + '\'' +
                ", workcellId='" + workcellId + '\'' +
                ", soNum='" + soNum + '\'' +
                ", soLineNum='" + soLineNum + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WmsDistributionQtyVO that = (WmsDistributionQtyVO) o;
        return Objects.equals(materialId, that.materialId) &&
                Objects.equals(materialCode, that.materialCode) &&
                Objects.equals(materialVersion, that.materialVersion) &&
                Objects.equals(workcellId, that.workcellId) &&
                Objects.equals(soNum, that.soNum) &&
                Objects.equals(soLineNum, that.soLineNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, materialCode, materialVersion, workcellId, soNum, soLineNum);
    }
}
