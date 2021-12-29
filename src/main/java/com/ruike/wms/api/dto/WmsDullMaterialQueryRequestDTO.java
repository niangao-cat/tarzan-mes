package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname DullMaterialQueryRequestDTO
 * @Description 呆滞物料报表查询传入参数
 * @Date 2019/10/29 17:23
 * @Author by {HuangYuBin}
 */
@ApiModel("呆滞物料报表查询传入参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WmsDullMaterialQueryRequestDTO {
	@ApiModelProperty(value = "工厂ID")
	private String siteId;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "仓库ID")
	private String warehouseId;
	@ApiModelProperty(value = "呆滞日期从")
	private String dullDateStart;
	@ApiModelProperty(value = "呆滞日期至")
	private String dullDateEnd;
	@ApiModelProperty(value = "实物条码")
	private String materialLotCode;
	@ApiModelProperty(value = "呆滞类型")
	private String dullType;
	@ApiModelProperty(value = "质量状态")
	private String qualityStatus;
}