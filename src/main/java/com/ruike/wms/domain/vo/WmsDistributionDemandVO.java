package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * 备料需求
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 14:38
 */
@Data
public class WmsDistributionDemandVO {
    @ApiModelProperty(value = "需求汇总维度ID")
    private Integer keyId;
    @ApiModelProperty(value = "组件物料ID")
    private String materialId;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "工厂")
    private String siteId;
    @ApiModelProperty(value = "产品版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "生产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "工段ID")
    private String workcellId;
    @ApiModelProperty(value = "工段编码")
    private String workcellCode;
    @ApiModelProperty(value = "配送基础数据ID")
    private String distributionBasicId;
    @ApiModelProperty(value = "最小包装")
    private String minPackage;
    @ApiModelProperty(value = "配送比例")
    private BigDecimal proportion;
    @ApiModelProperty(value = "库存水位")
    private BigDecimal inventoryLevel;
    @ApiModelProperty(value = "单次配送量")
    private BigDecimal oneQty;
    @ApiModelProperty(value = "配送货位")
    private String distributionLocator;
    @ApiModelProperty(value = "策略类型")
    @LovValue(lovCode = "WMS.DISTRIBUTION", meaningField = "distributionTypeMeaning")
    private String distributionType;
    @ApiModelProperty(value = "策略类型")
    private String distributionTypeMeaning;
    @ApiModelProperty(value = "仓库数量")
    private BigDecimal inventoryQty;
    @ApiModelProperty(value = "工段数量")
    private BigDecimal workcellQty;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty("组件编码")
    private String materialCode;
    @ApiModelProperty("组件名称")
    private String materialName;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty(value = "生产线名称")
    private String prodLineName;
    @ApiModelProperty("工段名称")
    private String workcellName;
    @ApiModelProperty("状态描述")
    private String statusMeaning;
    @ApiModelProperty("替代料")
    private String substituteMaterialCode;
    @ApiModelProperty("替代班次")
    private String substituteShift;
    @ApiModelProperty("替代料数量")
    private BigDecimal substituteQty;
    @ApiModelProperty("班次数量列表")
    private List<WmsDistributionDemandQtyVO> shiftQtyList;

    @ApiModelProperty("线边标识")
    private String flag;
}
