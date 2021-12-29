package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


/**
 * @Classname PrdDeliveryVO
 * @Description 产品出货VO
 * @Date 2019/9/25 17:03
 * @Author  weihua.liao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsPrdDeliveryVO {

	@ApiModelProperty("基地")
	private String isBase;

	@ApiModelProperty("站点ID")
	private List<String> siteIds;

	@ApiModelProperty("是否拼柜")
	private String lclFlag;

	@ApiModelProperty("单据行ID")
	private List<String> instructionIds;

	@ApiModelProperty("租户ID")
	private Long tenantId;

	@ApiModelProperty("单据ID")
	private List<String> instructionDocIds;

	@ApiModelProperty("单据ID")
	private String instructionDocId;

	@ApiModelProperty("箱号、封号必输标识")
	Boolean sealFlag;

}