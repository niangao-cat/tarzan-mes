package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 扫码返回VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/25 14:46
 */
@Data
public class QmsDocMaterialLotVO implements Serializable {

    private static final long serialVersionUID = 7497552003736136689L;

    @ApiModelProperty("物料批id")
    private String materialLotId;

    @ApiModelProperty("物料批编码")
    private String materialLotCode;

    @ApiModelProperty("送货单id")
    private String instructionDocId;

    @ApiModelProperty("送货单单号")
    private String instructionDocNum;

    @ApiModelProperty("质检单头id")
    private String iqcHeaderId;

    @ApiModelProperty("送货单行id")
    private String instructionLineDocId;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("数量")
    private Long primaryUomQty;

    @ApiModelProperty("最大数量")
    private Long maxPrimaryUomQty;

    @ApiModelProperty("单位")
    private String uomCode;

    @ApiModelProperty("质量状态")
    private String qualityStatus;

    @ApiModelProperty("质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty("供应商批次")
    private String lot;
}
