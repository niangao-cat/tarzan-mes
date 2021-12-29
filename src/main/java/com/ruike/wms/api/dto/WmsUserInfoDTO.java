package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname UserInfoDTO
 * @Description 用户信息
 * @Date 2019/9/24 10:47
 * @Author by {HuangYuBin}
 */
@ApiModel("用户信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsUserInfoDTO {
	@ApiModelProperty(value = "用户ID")
	Long id;
	@ApiModelProperty(value = "登录名")
	String loginName;
	@ApiModelProperty(value = "用户email")
	String email;
	@ApiModelProperty(value = "租户ID")
	Long organizationId;
	@ApiModelProperty(value = "真实姓名")
	String realName;
	@ApiModelProperty(value = "电话")
	String phone;
}