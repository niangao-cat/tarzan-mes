package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/12 13:55
 */
@Data
public class HmeCosMaterialReturnVO3 implements Serializable {

    private static final long serialVersionUID = -8610413231965407985L;

    @ApiModelProperty(value = "可退料数量")
    private BigDecimal return11Qty;

    @ApiModelProperty(value = "退料数量")
    private BigDecimal backQty;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "目标条码")
    private String targetMaterialLotCode;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "单位id")
    private String uomId;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "行号")
    private String lineNumber;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "退料条码列表")
    private List<HmeCosScanBarcodeVO> barcodeVOList;

    @ApiModelProperty(value = "热沉退料列表")
    private List<HmeCosReturnScanLineVO> hotSinkList;

    @ApiModelProperty(value = "打线退料列表")
    private List<HmeCosReturnScanLineVO> wireBondList;

    @ApiModelProperty(value = "芯片退料列表")
    private List<HmeCosReturnScanLineVO> cosReturnList;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

}
