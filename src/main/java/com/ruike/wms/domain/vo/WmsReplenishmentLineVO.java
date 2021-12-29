package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 补料单行生成信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/26 17:06
 */
@Data
public class WmsReplenishmentLineVO {
    @ApiModelProperty("行虚拟ID")
    private Integer keyId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("销售订单号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("补料数量")
    private BigDecimal replenishQty;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("仓库库存")
    private BigDecimal inventoryQty;
    @ApiModelProperty("线边库存")
    private BigDecimal inStockQty;
    @ApiModelProperty("可替代标识")
    private Boolean substituteAllowedFlag;
    @ApiModelProperty("备注")
    private String remark;
}
