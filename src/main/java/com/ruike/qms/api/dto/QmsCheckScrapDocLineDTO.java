package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检验报废-送货单
 *
 * @author jiangling.zheng@hand-china.com 2020-08-26 14:58
 */
@Data
public class QmsCheckScrapDocLineDTO {
    private static final long serialVersionUID = 4942253858816696526L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据编码")
    private String instructionDocNum;
    @ApiModelProperty("单据状态")
    private String instructionDocStatus;
    @ApiModelProperty("供应商ID")
    private String supplierId;
    @ApiModelProperty("供应商编码")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;
    @ApiModelProperty("加急标识")
    private String urgentFlag;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("指令Num")
    private String instructionNum;
    @ApiModelProperty("指令状态")
    private String instructionStatus;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("指令实绩明细ID")
    private String actualDetailId;
    @ApiModelProperty("备注")
    private String remark;
}
