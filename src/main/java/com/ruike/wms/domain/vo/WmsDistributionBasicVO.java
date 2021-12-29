package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 配送基础数据导入
 *
 * @author sanfeng.zhang@hand-china.com 2020/9/1 14:13
 */
@Data
public class WmsDistributionBasicVO implements Serializable {

    private static final long serialVersionUID = -2688581342667315027L;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点id")
    private String siteId;

    @ApiModelProperty(value = "物料组ID")
    private String materialGroupId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料组编码")
    private String materialGroupCode;

    @ApiModelProperty(value = "产线id")
    private String productionLineId;

    @ApiModelProperty(value = "产线编码")
    private String productionLineCode;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "策略类型")
    private String distributionType;

    @ApiModelProperty(value = "比例")
    private BigDecimal proportion;

    @ApiModelProperty(value = "库存水位")
    private BigDecimal inventoryLevel;

    @ApiModelProperty(value = "安全库存配送量")
    private BigDecimal everyQty;

    @ApiModelProperty(value = "最小包装量")
    private BigDecimal minimumPackageQty;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "是否启用线边库存计算逻辑")
    private String backflushFlag;

    @ApiModelProperty(value = "工段")
    private String workcellId;

    @ApiModelProperty(value = "工段编码")
    private String workcellCode;
}
