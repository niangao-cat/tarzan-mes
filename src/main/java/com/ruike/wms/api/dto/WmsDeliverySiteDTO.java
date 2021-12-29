package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname SiteDTO
 * @Description 工厂DTO
 * @Date 2019/10/5 15:57
 * @Author by weihua.liao
 */
@ApiModel("工厂")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsDeliverySiteDTO {
	@ApiModelProperty("仓库Id")
	private String siteId;
	@ApiModelProperty("仓库Code")
	private String siteCode;
	@ApiModelProperty("工厂名")
	private String siteName;
	@ApiModelProperty("是否默认")
	private String defaultOrganizationFlag;
}