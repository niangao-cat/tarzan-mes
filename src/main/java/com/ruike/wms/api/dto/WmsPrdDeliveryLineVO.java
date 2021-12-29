package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * @Classname PrdDeliveryQryDTO
 * @Description 送货单头收类型
 * @Date 2019/9/25 17:03
 * @Created by weihua.liao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsPrdDeliveryLineVO implements Serializable {

	private static final long serialVersionUID = -1903375814565788895L;

	@ApiModelProperty(value = "物料批ID")
	private String materialLotId;

	@ApiModelProperty(value = "出货单ID")
	private String sourceDocId;

	@ApiModelProperty(value = "送货单编码")
	private String instructionDocNum;

	@ApiModelProperty("主键ID ,表示唯一一条记录")
	private String instructionId;

	@ApiModelProperty(value = "指令编号")
	private String instructionNum;

	@ApiModelProperty(value = "打托详情")
	private String packDetail;

	@ApiModelProperty(value = "行号")
	private String instructionLineNum;

	@ApiModelProperty(value = "物料ID")
	private String materialId;

	@ApiModelProperty(value = "物料编码")
	private String materialCode;

	@ApiModelProperty(value = "物料描述")
	private String materialName;

	@ApiModelProperty(value = "实际数")
	private BigDecimal actualQty;

	@ApiModelProperty(value = "执行数")
	private BigDecimal executeQty;

	@ApiModelProperty(value = "可执行数")
	private BigDecimal enableExecuteQty;

	@ApiModelProperty(value = "产品货位")
	private String locatorCode;

	@ApiModelProperty(value = "目标仓库ID")
	private String locatorId;

	@ApiModelProperty(value = "状态")
	private String instructionStatus;

	@ApiModelProperty(value = "单位")
	private String primaryUomCode;

	@ApiModelProperty(value = "状态")
	@LovValue(value = "MT.MTLOT.STATUS", meaningField = "statusMeaning")
	private String status;

	@ApiModelProperty(value = "状态含义")
	private String statusMeaning;

	@ApiModelProperty(value = "需求数")
	private  String quantity;

	@ApiModelProperty(value = "工单ID")
	private String workOrderId;

	@ApiModelProperty(value = "工单ID")
	List<String> workOrderIds;

	@ApiModelProperty(value = "货位编码")
	List<String> locatorCodes;

	@ApiModelProperty(value = "货位编码")
	List<String> materialLotIds;

}