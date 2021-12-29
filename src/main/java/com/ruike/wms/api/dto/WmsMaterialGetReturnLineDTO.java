package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * @Classname MaterialGetReturnLineDTO
 * @Description 领退料单据行信息
 * @Date 2019/10/14 8:39
 * @Author by {HuangYuBin}
 */
@ApiModel("领退料单据行信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnLineDTO {
	@ApiModelProperty(value = "单据行号")
	private String instructionLineNum;
	@ApiModelProperty(value = "单据行ID")
	private String instructionId;
	@ApiModelProperty(value = "单据行状态")
	private String instructionStatus;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "物料编码")
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
	private String materialName;
	@ApiModelProperty(value = "需求数")
	private BigDecimal demandQuantity;
	@ApiModelProperty(value = "执行数")
	private BigDecimal executionQuantity;
	@ApiModelProperty(value = "单位")
	private String uomCode;
	@ApiModelProperty(value = "目标库位")
	private String targetLocator;
	@ApiModelProperty(value = "目标库位ID")
	private String targetLocatorId;
	@ApiModelProperty(value = "来源库位ID")
	private String currentLocatorId;
	@ApiModelProperty(value = "来源库位")
	private String currentLocator;
	@ApiModelProperty(value = "指定批次")
	private String lot;
	@ApiModelProperty(value = "色温bin")
	private String colorBin;
	@ApiModelProperty(value = "亮度bin")
	private String lightBin;
	@ApiModelProperty(value = "电压bin")
	private String voltageBin;
	@ApiModelProperty(value = "指定bin")
	private String bin;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "目标库位或仓库（前端不需要）")
	private String locatorId;
	@ApiModelProperty(value = "目标库位或仓库类型（前端不需要）")
	private String locatorCategory;
	@ApiModelProperty(value = "目标库位或仓库编号（前端不需要）")
	private String locatorCode;
	@ApiModelProperty(value = "报废原因")
	@LovValue(lovCode = "SCRAP_REASON",meaningField="scrapReasonCode",defaultMeaning = "无")
	private String scrapReason ;
	@ApiModelProperty(value = "报废原因Code")
	private String scrapReasonCode ;
	@ApiModelProperty(value = "单位")
	private String uomId;
	@ApiModelProperty(value = "等级编号")
	private String gradeCode;
	public String getBin() {
		if (StringUtils.isEmpty(colorBin)){
			colorBin = "无";
		}
		if (StringUtils.isEmpty(lightBin)){
			lightBin = "无";
		}
		if (StringUtils.isEmpty(voltageBin)){
			voltageBin = "无";
		}
		return colorBin+"/"+lightBin+"/"+voltageBin;
	}
}