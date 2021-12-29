package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @Classname MaterialGetReturnLineSaveDTO
 * @Description 新建领退料单据行信息
 * @Date 2019/10/15 9:46
 * @Author by {HuangYuBin}
 */
@ApiModel("新建领退料单据行信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnLineSaveDTO {
	@ApiModelProperty(value = "单据号")
	private String instructionDocNum;
	@ApiModelProperty(value = "单据ID")
	private String instructionDocId;
	@ApiModelProperty(value = "来源工厂")
	private String currentSite;
	@ApiModelProperty(value = "目标工厂")
	private String targetSite;
	@ApiModelProperty(value = "工厂ID")
	private String siteId;
	@ApiModelProperty(value = "来源仓库")
	private String currentWarehouse;
	@ApiModelProperty(value = "目标仓库")
	private String targetWarehouse;
	@ApiModelProperty(value = "单据类型")
	private String typeCode;
	@ApiModelProperty(value = "行信息列表")
	List<Line> lineList;
	@ApiModelProperty(value = "成本中心ID")
	private String costCenterId;
	@ApiModelProperty(value = "创建头信息时的事件组id（新建行时再传入）")
	private String eventRequestId;
	@ApiModelProperty(value = "创建头信息时的事件id（新建行时再传入）")
	private String eventId;
	@ApiModelProperty(value = "单据状态")
	private String status;
	@ApiModelProperty(value = "判断下达或取消下达")
	private Boolean signal;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "质量状态")
	private String returnQcFlag;
	@ApiModelProperty(value = "报废部门ID")
	private String scrapDepartment;
	@ApiModel("行信息")
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class Line{
		@ApiModelProperty(value = "单据行ID")
		private String instructionId;
		@ApiModelProperty(value = "单据头ID")
		private String instructionDocId;
		@ApiModelProperty(value = "单据行号(序号)")
		private String instructionLineNum;
		@ApiModelProperty(value = "物料ID")
		private String materialId;
		@ApiModelProperty(value = "物料编号")
		private String materialCode;
		@ApiModelProperty(value = "单位ID")
		private String uomId;
		@ApiModelProperty(value = "需求数")
		private Double demandQuantity;
		@ApiModelProperty(value = "目标仓库ID")
		private String targetWarehouse;
		@ApiModelProperty(value = "目标库位ID")
		private String targetLocator;
		@ApiModelProperty(value = "来源库位ID")
		private String currentLocator;
		@ApiModelProperty(value = "指定批次")
		private String lot;
		@ApiModelProperty(value = "等级编码")
		private String gradeCode;
		@ApiModelProperty(value = "备注")
		private String remark;
		@ApiModelProperty(value = "报废原因")
		private String scrapReason;
		@ApiModelProperty(value = "指令状态")
		private String instructionStatus;
		@ApiModelProperty(value = "报废原因Code")
		private String scrapReasonCode;
		public String getLot() {
			if(this.lot==null)
			{
				return "";
			}
			return lot;
		}
		public String getGradeCode() {
			if(this.gradeCode==null)
			{
				return "";
			}
			return gradeCode;
		}
		public String getScrapReason() {
			if(this.scrapReason==null)
			{
				return "";
			}
			return scrapReason;
		}
	}

}