package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MaterialGetReturnRequestDTO
 * @Description 领退料单查询请求参数
 * @Date 2019/10/12 14:41
 * @Author by {HuangYuBin}
 */
@ApiModel("领退料单查询请求参数")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnRequestDTO {
	@ApiModelProperty(value = "单据号")
	private String instructionDocNum;

	@ApiModelProperty(value = "单据状态")
	private String status;

	@ApiModelProperty(value = "单据类型")
	private String typeCode;

	@ApiModelProperty(value = "工厂ID")
	private String siteId;

	@ApiModelProperty(value = "来源仓库ID")
	private String sourceLocatorId;

	@ApiModelProperty(value = "目标仓库ID")
	private String targetLocatorId;

	@ApiModelProperty(value = "成本中心ID")
	private String costCenterId;

	@ApiModelProperty(value = "创建时间从")
	private String creationDateStart;

	@ApiModelProperty(value = "创建时间至")
	private String creationDateEnd;

	@ApiModelProperty(value = "申请人")
	private String applier;

	@ApiModelProperty(value = "执行时间从")
	private String executionDateStart;

	@ApiModelProperty(value = "执行时间至")
	private String executionDateEnd;

	@ApiModelProperty(value = "执行人")
	private String execution;

	@ApiModelProperty(value = "物料ID")
	private String materialId;

	@ApiModelProperty(value = "报废原因")
	private String scrapReason;
}