package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * 备料需求单据行
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 21:30
 */
@Data
public class WmsPrepareExecInsVO {
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("配送单行ID")
    private String instructionId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty("配送单行状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty("配送单行状态")
    private String instructionStatusMeaning;
    @ApiModelProperty("行需求数量")
    private BigDecimal instructionQty;
    @ApiModelProperty("历史执行数量")
    private BigDecimal actualQty;
    @ApiModelProperty("条码个数")
    private Integer materialLotCount;
    @ApiModelProperty("单位")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("备料货位ID")
    private String locatorId;
    @ApiModelProperty("备料货位")
    private String locatorCode;
    @ApiModelProperty("销售订单号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("站点ID")
    private String siteId;
}
