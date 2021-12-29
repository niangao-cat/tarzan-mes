package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * IQC检验平台物料批
 * <p/>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 13:53
 */
@Data
public class QmsIqcMaterialLotVO {
    @ApiModelProperty("IQC检验头")
    private String iqcHeaderId;
    @ApiModelProperty("单据行ID")
    private String instructionId;
    @ApiModelProperty("单据实际ID")
    private String actualId;
    @ApiModelProperty("实际明细ID")
    private String actualDetailId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("条码数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("供应商批次")
    private String supplierLot;
    @ApiModelProperty("批次")
    private String lot;
}
