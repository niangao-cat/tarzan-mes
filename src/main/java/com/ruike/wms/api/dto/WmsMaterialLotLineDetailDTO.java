package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * MaterialLotLineDetailDTO
 *
 * @author liyuan.lv@hand-china.com 2020/04/18 0:08
 */
@ApiModel("实物条码信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialLotLineDetailDTO {

	@ApiModelProperty(value = "实物条码ID")
	private String materialLotId;
	@ApiModelProperty(value = "实物条码编号")
	private String materialLotCode;
	@ApiModelProperty("表格内条码数量")
	private BigDecimal primaryUomQty;
	@ApiModelProperty("表格内数量单位ID")
	private String uomId;
	@ApiModelProperty("表格内数量单位编码")
	private String uomCode;
	@ApiModelProperty("状态")
	private String status;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
	private String materialName;
	@ApiModelProperty("已确认数")
	private BigDecimal completeQuantity = BigDecimal.valueOf(0);
	@ApiModelProperty("总计")
	private BigDecimal sumQuantity = BigDecimal.valueOf(0);
	@ApiModelProperty("检验报废数量")
	private BigDecimal inspectScrapQty = BigDecimal.valueOf(0);
	@ApiModelProperty(value = "批次")
	private String lot;
	@ApiModelProperty("供应商id")
	private String supplierId;
	@ApiModelProperty("供应商code")
	private String supplierCode;
	@ApiModelProperty("供应商名称")
	private String supplierName;
	@ApiModelProperty("条码货位ID")
	private String materialLotLocatorId;
	@ApiModelProperty("条码货位code")
	private String materialLotLocatorCode;
	@ApiModelProperty("条码货位名称")
	private String materialLotLocatorName;
	@ApiModelProperty("库位code")
	private String warehouseCode;
	@ApiModelProperty("质量状态")
	@LovValue(value = "WMS.MTLOT.QUALITY_STATUS",meaningField ="qualityStatusMeaning" )
	private String qualityStatus;
	@ApiModelProperty("质量状态Meaning")
	private String qualityStatusMeaning;
	@ApiModelProperty("条码状态")
	@LovValue(value = "WMS.MTLOT.STATUS",meaningField ="materialLotStatusMeaning" )
	private String materialLotStatus;
	@ApiModelProperty("条码状态Meaning")
	private String materialLotStatusMeaning;

}