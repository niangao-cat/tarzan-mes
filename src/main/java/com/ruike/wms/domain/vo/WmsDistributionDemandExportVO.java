package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 配送需求导出
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 15:35
 */
@Data
public class WmsDistributionDemandExportVO {
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty("班次日期")
    private LocalDate shiftDate;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal requirementQty;
    @ApiModelProperty(value = "数量列表")
    List<WmsDistributionDemandExportQtyVO> qtyList;

    public static WmsDistributionDemandExportVO summaryRow(WmsDistributionDemandExportVO obj) {
        WmsDistributionDemandExportVO sum = new WmsDistributionDemandExportVO();
        sum.setMaterialId(obj.getMaterialId());
        sum.setMaterialCode(obj.getMaterialCode());
        sum.setMaterialName(obj.getMaterialName());
        sum.setMaterialVersion(obj.getMaterialVersion());
        sum.setSoNum(obj.getSoNum());
        sum.setSoLineNum(obj.getSoLineNum());
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WmsDistributionDemandExportVO that = (WmsDistributionDemandExportVO) o;
        return Objects.equals(materialId, that.materialId) &&
                Objects.equals(materialCode, that.materialCode) &&
                Objects.equals(materialName, that.materialName) &&
                Objects.equals(materialVersion, that.materialVersion) &&
                Objects.equals(soNum, that.soNum) &&
                Objects.equals(soLineNum, that.soLineNum) &&
                Objects.equals(shiftDate, that.shiftDate) &&
                Objects.equals(requirementQty, that.requirementQty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, materialCode, materialName, materialVersion, soNum, soLineNum, shiftDate, requirementQty);
    }
}
