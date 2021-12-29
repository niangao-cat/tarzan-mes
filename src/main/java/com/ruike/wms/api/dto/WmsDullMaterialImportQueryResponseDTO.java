package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * @Classname DullMaterialImportQueryResponseDTO
 * @Description 呆滞物料导入查询传出参数
 * @Date 2019/10/31 13:15
 * @Author by {HuangYuBin}
 */
@ApiModel("呆滞物料导入查询传出参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WmsDullMaterialImportQueryResponseDTO {
	@ApiModelProperty(value = "实物条码ID（主键）")
	private String materialLotId;
	@ApiModelProperty(value = "实物条码")
	private String materialLotCode;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "物料编码")
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
	private String materialName;
	@ApiModelProperty(value = "呆滞标记")
	private String dullFlag;
	@ApiModelProperty(value = "呆滞类型")
	private String dullType;
	@ApiModelProperty(value = "批次")
	private String lot;
	@ApiModelProperty(value = "数量")
	private Double primaryUomQty;
	@ApiModelProperty(value = "单位ID")
	private String uomId;
	@ApiModelProperty(value = "单位编码")
	private String uomCode;
	@LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "status", defaultMeaning = "无")
	@ApiModelProperty(value = "条码状态")
	private String status;
	@LovValue(lovCode = "Z.MTLOT.QUALITY_STATUS.G", meaningField = "qualityStatus", defaultMeaning = "无")
	@ApiModelProperty(value = "质量状态")
	private String qualityStatus;
	@ApiModelProperty(value = "仓库ID")
	private String warehouseId;
	@ApiModelProperty(value = "仓库编码")
	private String warehouseCode;
	@ApiModelProperty(value = "货位Id")
	private String locatorId;
	@ApiModelProperty(value = "货位编码")
	private String locatorCode;
	@ApiModelProperty(value = "容器ID")
	private String containerId;
	@ApiModelProperty(value = "容器条码")
	private String containerCode;
	@ApiModelProperty(value = "站点ID（前端无需展示，作为保存的传入参数）")
	private String siteId;
}