package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MaterialGetReturnSaveResponseDTO
 * @Description 新建领退料单据头信息输出参数
 * @Date 2019/10/15 11:02
 * @Author by {HuangYuBin}
 */
@ApiModel("新建领退料单据头信息输出参数")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnSaveResponseDTO {
	@ApiModelProperty(value = "单据号")
	private String instructionDocNum;
	@ApiModelProperty(value = "单据ID")
	private String instructionDocId;
	@ApiModelProperty(value = "创建头信息时的事件组id（新建行时再传入）")
	private String eventRequestId;
	@ApiModelProperty(value = "创建头信息时的事件id（新建行时再传入）")
	private String eventId;
	@ApiModelProperty(value = "工厂ID")
	private String siteId;
	@ApiModelProperty(value = "单据类型")
	private String typeCode;
	@ApiModelProperty(value = "成本中心ID")
	private String costCenterId;
	@ApiModelProperty(value = "单据状态")
	private String instructionDocStatus;
	@ApiModelProperty(value = "来源仓库")
	private String currentWarehouse;
	@ApiModelProperty(value = "目标仓库")
	private String targetWarehouse;

}