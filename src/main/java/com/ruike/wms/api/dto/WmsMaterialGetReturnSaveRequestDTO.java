package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MaterialGetReturnSaveRequestDTO
 * @Description 新建领退料单据头信息输入参数
 * @Date 2019/10/15 9:06
 * @Author by {HuangYuBin}
 */
@ApiModel("新建领退料单据头信息输入参数")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnSaveRequestDTO {
	@ApiModelProperty(value = "单据类型")
	private String typeCode;
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
	@ApiModelProperty(value = "成本中心ID")
	private String costCenterId;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "质量状态")
	private String returnQcFlag;
	@ApiModelProperty(value = "报废部门ID")
	private String scrapDepartment;
}