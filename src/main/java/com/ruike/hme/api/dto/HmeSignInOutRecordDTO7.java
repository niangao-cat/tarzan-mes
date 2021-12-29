package com.ruike.hme.api.dto;


import java.io.Serializable;
import java.util.Date;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工上下岗员工信息
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO7 implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;
	@ApiModelProperty(value = "日历id")
    private String calendarId;
	@ApiModelProperty(value = "班次编码")
    private String shiftCode;
    @ApiModelProperty(value = "班次开始时间")
    private Date shiftStartTime;
    @ApiModelProperty(value = "班次结束时间")
    private Date shiftEndTime;
    @ApiModelProperty(value = "班次内休息时间")
    private Double restTime;
}
