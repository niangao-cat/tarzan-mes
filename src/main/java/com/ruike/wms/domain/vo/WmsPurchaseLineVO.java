package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.instruction.domain.entity.MtInstruction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/9 22:04
 */
@Data
public class WmsPurchaseLineVO extends MtInstruction implements Serializable {

    private static final long serialVersionUID = -3847222965236089449L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "执行数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "条码数量")
    private BigDecimal codeQty;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "Z_INSTRUCTION_DOC_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty(value = "状态含义")
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "退货仓库编码")
    private String locatorCode;

    @ApiModelProperty(value = "退货仓库名称")
    private String locatorName;

    @ApiModelProperty(value = "销售订单")
    private String deliveryNumber;

    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;

    @ApiModelProperty(value = "条码信息")
    private List<WmsPurchaseCodeDetailsVO> materialLotList;
}
