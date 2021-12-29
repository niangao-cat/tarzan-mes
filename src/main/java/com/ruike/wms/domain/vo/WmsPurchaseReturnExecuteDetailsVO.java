package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/10 20:51
 */
@Data
public class WmsPurchaseReturnExecuteDetailsVO implements Serializable {

    private static final long serialVersionUID = 5477732196591896114L;

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

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "MT.PO_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String docStatus;

    @ApiModelProperty(value = "状态含义")
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "退货仓库编码")
    private String locatorCode;

    @ApiModelProperty(value = "退货仓库名称")
    private String locatorName;

    @ApiModelProperty(value = "物料批信息")
    private List<WmsPurchaseCodeDetailsVO> lineList;

}
