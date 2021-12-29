package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname SiteDTO
 * @Description 站点（工厂）
 * @Date 2019/10/14 8:22
 * @Author by {HuangYuBin}
 */
@ApiModel("站点（工厂）")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsSiteDTO {
	@ApiModelProperty(value = "站点ID")
	private String siteId;
	@ApiModelProperty(value = "站点编码")
	private String siteCode;
	@ApiModelProperty(value = "站点描述")
	private String description;
}