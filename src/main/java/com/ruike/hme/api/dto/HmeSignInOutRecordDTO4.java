package com.ruike.hme.api.dto;


import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工上下岗员工信息
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO4 implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;
	@ApiModelProperty(value = "日期")
    private int day;
    @ApiModelProperty(value = "是否缺勤")
    private Boolean isWork;
}
