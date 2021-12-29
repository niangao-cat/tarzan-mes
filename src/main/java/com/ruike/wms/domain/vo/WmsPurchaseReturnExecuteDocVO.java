package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/9 20:37
 */
@Data
public class WmsPurchaseReturnExecuteDocVO implements Serializable {

    private static final long serialVersionUID = 7361448803053151550L;

    @ApiModelProperty(value = "单据头id")
    private String instructionDocId;

    @ApiModelProperty(value = "单据单号")
    private String instructionDocNum;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "Z_INSTRUCTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty(value = "单据状态含义")
    private String instructionDocStatusMeaning;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "行信息")
    private List<WmsPurchaseLineVO> linePurchaseList;
}
