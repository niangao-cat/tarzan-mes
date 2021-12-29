package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.util.Date;

/**
 * @Classname DullMaterialQueryResponseDTO
 * @Description 呆滞物料报表查询传出参数
 * @Date 2019/10/29 17:23
 * @Author by {HuangYuBin}
 */
@ApiModel("呆滞物料报表查询传出参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExcelSheet(zh = "呆滞物料报表")
public class WmsDullMaterialQueryResponseDTO {
	@ApiModelProperty(value = "实物条码ID（主键）")
	private String materialLotId;
	@ApiModelProperty(value = "实物条码")
	@ExcelColumn(zh = "实物条码",order = 1)
	private String materialLotCode;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "物料编码")
	@ExcelColumn(zh = "物料编码",order = 2)
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
	@ExcelColumn(zh = "物料描述",order = 3)
	private String materialName;
	@ApiModelProperty(value = "批次")
	@ExcelColumn(zh = "批次",order = 4)
	private String lot;
	@ApiModelProperty(value = "数量")
	@ExcelColumn(zh = "数量",order = 5)
	private Double primaryUomQty;
	@ApiModelProperty(value = "单位ID")
	private String uomId;
	@ApiModelProperty(value = "单位编码")
	@ExcelColumn(zh = "单位",order = 6)
	private String uomCode;
	@ApiModelProperty(value = "呆滞日期")
	@ExcelColumn(zh = "呆滞日期",order = 7, pattern = BaseConstants.Pattern.DATETIME)
	private Date dullDate;
	@ApiModelProperty(value = "呆滞类型")
	@ExcelColumn(zh = "呆滞类型",order = 8)
	private String dullType;
	@ApiModelProperty(value = "评审结果")
	@ExcelColumn(zh = "评审结果",order = 9)
	private String reviewResult;
	@LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "status", defaultMeaning = "无")
	@ApiModelProperty(value = "条码状态")
	@ExcelColumn(zh = "条码状态",order = 10)
	private String status;
	@LovValue(lovCode = "Z.MTLOT.QUALITY_STATUS.G", meaningField = "qualityStatus", defaultMeaning = "无")
	@ApiModelProperty(value = "质量状态")
	@ExcelColumn(zh = "质量状态",order = 11)
	private String qualityStatus;
	@ApiModelProperty(value = "仓库ID")
	private String warehouseId;
	@ApiModelProperty(value = "仓库编码")
	@ExcelColumn(zh = "仓库",order = 12)
	private String warehouseCode;
	@ApiModelProperty(value = "货位Id")
	private String locatorId;
	@ApiModelProperty(value = "货位编码")
	@ExcelColumn(zh = "货位",order = 13)
	private String locatorCode;
	@ApiModelProperty(value = "容器ID")
	private String containerId;
	@ApiModelProperty(value = "容器编码")
	@ExcelColumn(zh = "容器条码",order = 14)
	private String containerCode;
}