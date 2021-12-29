package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;


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
public class WmsPrdDeliveryLineDetailDTO implements Serializable {

	private static final long serialVersionUID = -1903375814565788895L;


	@ApiModelProperty("主键ID ,表示唯一一条记录")
	private String instructionId;

	@ApiModelProperty(value = "指令编号")
	private String instructionNum;

	@ApiModelProperty(value = "出货单ID")
	private String sourceDocId;

	@ApiModelProperty(value = "打托详情")
	private String packDetail;

	@ApiModelProperty(value = "行号")
	private String instructionLineNum;

	@ApiModelProperty(value = "物料ID")
	private String materialId;

	@ApiModelProperty(value = "状态")
	private String instructionStatus;

	@ApiModelProperty(value = "指令数量")
	private Double quantity;

	@ApiModelProperty(value = "实际数")
	private Double actualQty;

	@ApiModelProperty(value = "执行数")
	private Double executeQty;

	@ApiModelProperty(value = "可执行数")
	private Double enableExecuteQty;

	@ApiModelProperty(value = "产品货位")
	private String locatorCode;

	@ApiModelProperty(value = "目标仓库ID")
	private String locatorId;

	@ApiModelProperty(value = "打描的条码")
	private String barCode;

	@ApiModelProperty(value = "条码类型")
	private String codeType;

	@ApiModelProperty(value = "条码ID")
	private String codeId;

	@ApiModelProperty(value = "批次")
	private String lot;

	@ApiModelProperty(value = "状态")
	@LovValue(value = "MT.MTLOT.STATUS", meaningField = "statusMeaning")
	private String status;

	@ApiModelProperty(value = "状态含义")
	private String statusMeaning;

	@ApiModelProperty(value = "批次ID")
	private String materialLotId;

	@ApiModelProperty(value = "物料批数量")
	private Double primaryUomQty;

	@ApiModelProperty(value = "物料批单位ID")
	private String primaryUomId;

	@ApiModelProperty(value = "实物条码")
	private String materialLotCode;

	@ApiModelProperty(value = "容器条码")
	private String containerCode;

	@ApiModelProperty(value = "站点编码")
	private String siteCode;

	@ApiModelProperty(value = "站点ID")
	private String siteId;

	@ApiModelProperty(value = "物料编码")
	private String materialCode;

	@ApiModelProperty(value = "物料描述")
	private String materialName;

	@ApiModelProperty(value = "单位")
	private String primaryUomCode;

	@ApiModelProperty(value = "工单ID")
	private String workOrderId;

	@ApiModelProperty(value = "销售订单类型")
	private String sourceOrderType;

	@ApiModelProperty(value = "事务ID")
	private String transactionId;

	@ApiModelProperty(value = "送货单编码")
	private String instructionDocNum;

	@ApiModelProperty(value = "是否为AGV操作类型")
	private String isAgv;
}