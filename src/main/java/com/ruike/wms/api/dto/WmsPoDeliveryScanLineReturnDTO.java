package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.instruction.domain.entity.MtInstruction;

import java.math.BigDecimal;

/**
 * <p>
 * 采购接收行
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/1 16:36
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WmsPoDeliveryScanLineReturnDTO extends MtInstruction {

    private static final long serialVersionUID = 7621959903661612042L;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "接收仓库编码")
    private String toLocatorCode;

    @ApiModelProperty(value = "接收仓库")
    private String toLocatorName;

    @ApiModelProperty(value = "料废调换数量")
    private BigDecimal exchangeQty;

    @ApiModelProperty(value = "已料废调换数量")
    private BigDecimal exchangedQty;

    @ApiModelProperty(value = "料废调换标志")
    private String exchangeFlag;

    @ApiModelProperty(value = "特采表示")
    private String uaiFlag;

    @ApiModelProperty(value = "条码数量")
    private BigDecimal codeQty;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.DELIVERY_DOC_LINE.STATUS", meaningField = "statusMeaning")
    private String instructionStatus1;

    @ApiModelProperty(value = "单据状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "历史执行数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "免检标识")
    private String exemptionFlag;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

}