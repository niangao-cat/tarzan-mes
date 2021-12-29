package com.ruike.hme.api.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工上下岗员工信息
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO10 implements Serializable {
    private static final long serialVersionUID = 7878732768964491855L;

	@ApiModelProperty(value = "用户",required = true)
	private Long userId;
	@ApiModelProperty(value = "员工ID",required = true)
	private Long employeeId;
    @ApiModelProperty(value = "工段", required = true)
    private String workcellId;
    @ApiModelProperty(value = "年份",required = true)
    private Integer year;
    @ApiModelProperty(value = "月份",required = true)
    private Integer month;
   
}
