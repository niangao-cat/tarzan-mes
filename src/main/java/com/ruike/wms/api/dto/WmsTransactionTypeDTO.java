package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname TransactionTypeDTO
 * @Description 事务类型维护参数
 * @Date 2020/04/24 9:39
 * @Author by liyuan.lv
 */
@ApiModel("事务类型维护参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WmsTransactionTypeDTO {
	@ApiModelProperty("事务类型ID")
	private String transactionTypeId;
	@ApiModelProperty(value = "事务类型编码")
	private String transactionTypeCode;
	@ApiModelProperty(value = "描述")
	private String description;
	@ApiModelProperty(value = "移动类型")
	private String moveType;
	@ApiModelProperty(value = "是否回传ERP（传入N/Y）")
	private String processErpFlag;
	@ApiModelProperty(value = "是否有效（传入N/Y）")
	private String enableFlag;
	@ApiModelProperty(value = "更新操作传入版本号")
	private Long objectVersionNumber;
}