package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * @Classname PrdDeliveryQryDTO
 * @Description 送货单头收类型
 * @Date 2019/9/25 17:03
 * @Created by weihua.liao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsPrdDeliveryQryDTO implements Serializable {

	private static final long serialVersionUID = -1903375814565788895L;

	@ApiModelProperty(value = "送货单编码")
	@NotBlank(message = "送货单编码不能为空")
	private String instructionDocNum;

	@ApiModelProperty(value = "送货单状态")
	private String instructionDocStatus;

	@ApiModelProperty(value = "工厂代码")
	@NotBlank(message = "工厂代码不能为空")
	private String siteId;

	@ApiModelProperty(value = "供应商编码")
	@NotBlank(message = "供应商编码不能为空")
	private String supplierCode;

	@ApiModelProperty(value = "来源单据类型")
	private String sourceOrderType;


	@ApiModelProperty(value = "需求时间")
	private String demandTime;

	@ApiModelProperty(value = "日期标识")
	private String dateFlag;

}