package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.instruction.domain.entity.MtInstruction;

import java.math.BigDecimal;

/**
 * @Classname InstructionDTO
 * @Description 指令列表对象
 * @Date 2019/9/20 14:33
 * @Author by {HuangYuBin}
 */
@ApiModel("指令列表")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WmsInstructionDTO extends MtInstruction {

	@ApiModelProperty(value = "实际接收数量")
	private BigDecimal actualQty;
	@ApiModelProperty(value = "采购订单号")
	private String poNum;
	@ApiModelProperty(value = "采购订单行号")
	private String poLineNum;


	@ApiModelProperty(value = "退货数量")
	private BigDecimal primaryUomQtyNg;
	@ApiModelProperty(value = "让步放行数量")
	private BigDecimal primaryUomQtyRelease;
	@ApiModelProperty(value = "合格数量")
	private BigDecimal primaryUomQtyOk;
	@ApiModelProperty(value = "实际入库数量")
	private BigDecimal primaryUomQtyInstock;
	@ApiModelProperty(value = "已执行数量")
	private Double coverQty;
	@ApiModelProperty(value = "已入库数量")
	private Double stockedQty;
	@ApiModelProperty(value = "物料编码")
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
	private String materialName;
	@ApiModelProperty(value = "送货数量")
	private Double quantity;
	@ApiModelProperty(value = "送货单行号")
	private String instructionNum;
	@ApiModelProperty(value = "单位编码")
	private String uomCode;
	@ApiModelProperty(value = "单位描述")
	private String uomName;
	@ApiModelProperty(value = "行号")
	private String instructionLineNum;
	@ApiModelProperty(value = "物料版本")
	private String materialVersion;
	@ApiModelProperty(value = "料废调换数量")
	private String exchangeQty;
	@ApiModelProperty(value = "特采标识")
	private String uaiFlag;
	@ApiModelProperty(value = "已接收数量")
	private String receivedQty;
	@ApiModelProperty(value = "已料废调换数量")
	private String exchangedQty;


	@ApiModelProperty(value = "状态")
	@LovValue(value = "WMS.DELIVERY_DOC_LINE_RFS.STATUS", meaningField = "instructionStatus1Meaning")
	private String instructionStatus1;

	@ApiModelProperty(value = "状态含义")
	private String instructionStatus1Meaning;

	@LovValue(value = "WMS.DELIVERY_DOC_LINE_TOL.STATUS", meaningField = "instructionStatus2Meaning")
	private String instructionStatus2;

	private String instructionStatus2Meaning;

	@ApiModelProperty(value = "soNum")
	private String soNum;

	@ApiModelProperty(value = "soLineNum")
	private String soLineNum;

	@ApiModelProperty(value = "IQC版本")
	private String iqcVersion;

    @ApiModelProperty(value = "接收仓库Id")
    private String locatorId;
    @ApiModelProperty(value = "接收仓库编码")
    private String locatorCode;
    @ApiModelProperty(value = "接收仓库描述")
    private String locatorName;
}