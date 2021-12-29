package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/12 10:41
 */
@Data
public class HmeCosScanBarcodeVO implements Serializable {

    private static final long serialVersionUID = 1752274190448454131L;

    @ApiModelProperty(value = "条码id")
    private String materialLotId;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "在制品")
    private String mfFlag;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "数量")
    private Double qty;

    @ApiModelProperty(value = "WEFER")
    private String waferNum;

    @ApiModelProperty(value = "工单id")
    private String workOrderId;

    @ApiModelProperty(value = "供应商")
    private String supplierId;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "热沉信息列表")
    private List<HmeCosReturnScanLineVO> hotSinkList;

    @ApiModelProperty(value = "打线信息列表")
    private List<HmeCosReturnScanLineVO> wireBondList;

    @ApiModelProperty(value = "芯片退料列表")
    private List<HmeCosReturnScanLineVO> cosReturnList;

    @ApiModelProperty(value = "退料类型")
    private String returnType;
}
