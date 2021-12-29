package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname DullMaterialImportQueryRequestDTO
 * @Description 呆滞物料导入查询传入参数
 * @Date 2019/10/31 13:15
 * @Author by {HuangYuBin}
 */
@ApiModel("呆滞物料导入查询传入参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WmsDullMaterialImportQueryRequestDTO {
	@ApiModelProperty(value = "工厂ID")
	private String siteId;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "批次")
	private String lot;
	@ApiModelProperty(value = "工单号")
	private String workOrderNum;
	@ApiModelProperty(value = "采购订单号")
	private String poNum;

}