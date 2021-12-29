package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;


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
public class WmsPrdDeliveryLineDTO implements Serializable {

	private static final long serialVersionUID = -1903375814565788895L;

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

	@ApiModelProperty(value = "已扫码")
	private String scanned;

	@ApiModelProperty(value = "销售订单类型")
	private String sourceOrderType;

	@ApiModelProperty(value = "工厂")
	private String siteName;

	@ApiModelProperty("箱号、封号必输标识")
	Boolean sealFlag;

	@ApiModelProperty("主界面箱号")
	String containerNum;

	@ApiModelProperty("主界面封号")
	String sealNumber;
}