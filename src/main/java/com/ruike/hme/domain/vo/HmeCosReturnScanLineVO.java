package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * COS退料扫描行信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/12 11:06
 */
@Data
public class HmeCosReturnScanLineVO {
    @ApiModelProperty(value = "序号")
    private Integer sequenceNum;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "单位Id")
    private String uomId;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位用量")
    private BigDecimal usageQty;

    @ApiModelProperty(value = "可退料数量")
    private BigDecimal availableQty;

    @ApiModelProperty(value = "供应商Id")
    private String supplierId;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "退料数量")
    private BigDecimal returnQty;

    @ApiModelProperty(value = "目标条码Id")
    private String targetMaterialLotId;

    @ApiModelProperty(value = "目标条码")
    private String targetMaterialLot;

    @ApiModelProperty(value = "热沉退料/报废标识 Y-退料 N-报废")
    private String hotSinkFlag;

    @ApiModelProperty(value = "打线退料/报废标识 Y-退料 N-报废")
    private String wireBondFlag;

    @ApiModelProperty(value = "芯片退料/报废标识 Y-退料 N-报废")
    private String cosReturnFlag;

    @ApiModelProperty(value = "装载数量")
    private BigDecimal totalCosNum;
}
