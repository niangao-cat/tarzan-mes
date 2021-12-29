package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * @Classname MaterialGetReturnLineDetailDTO
 * @Description 领退料单据行明细信息
 * @Date 2019/10/14 8:59
 * @Author by {HuangYuBin}
 */
@ApiModel("领退料单据行明细信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnLineDetailDTO {
	@ApiModelProperty(value = "单据行号")
	private String instructionLineNum;
	@ApiModelProperty(value = "条码ID")
	private String materialLotId;
	@ApiModelProperty(value = "条码号")
	private String materialLotCode;
	@LovValue(lovCode = "Z.MTLOT.STATUS.G",meaningField="materialLotStatus",defaultMeaning = "无")
	@ApiModelProperty(value = "条码状态")
	private String materialLotStatus;
	@ApiModelProperty(value = "条码数量")
	private String materialLotQty;
	@ApiModelProperty(value = "单位")
	private String uomCode;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "物料编码")
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
	private String materialName;
	@ApiModelProperty(value = "批次")
	private String lot;
	@ApiModelProperty(value = "仓库编码")
	private String locatorCode;
	@ApiModelProperty(value = "仓库ID")
	private String locatorId;
	@ApiModelProperty(value = "库位编码")
	private String subLocatorCode;
	@ApiModelProperty(value = "库位ID")
	private String subLocatorId;
	@ApiModelProperty(value = "色温bin")
	private String colorBin;
	@ApiModelProperty(value = "亮度bin")
	private String lightBin;
	@ApiModelProperty(value = "电压bin")
	private String voltageBin;
	@ApiModelProperty(value = "指定bin")
	private String bin;

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