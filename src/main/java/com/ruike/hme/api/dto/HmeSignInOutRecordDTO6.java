package com.ruike.hme.api.dto;


import java.io.Serializable;
import java.util.List;

import com.ruike.hme.domain.entity.HmeSignInOutRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工上下岗员工信息
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO6 implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;

	@ApiModelProperty(value = "所选日期")
	private String choiceTime;
    @ApiModelProperty(value = "班次")
    private String shiftcode;
    @ApiModelProperty(value = "当前班次是否可选")
    private boolean choice;
    @ApiModelProperty(value = "起止时间")
    private String startAndEndTime;
    @ApiModelProperty(value = "开始按键状态")
    private String startSwitch;
    @ApiModelProperty(value = "开始是否可以重新的上岗")
    private boolean startAgain;
    @ApiModelProperty(value = "开始名称")
    private String startName;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束按键状态")
    private String closeSwitch;
    @ApiModelProperty(value = "结束时间")
    private String closeTime;
    @ApiModelProperty(value = "累计时间")
    private String duration;
    @ApiModelProperty(value = "状态")
    private String operation;
    @ApiModelProperty(value = "当前班次relId")
    private String relId;
    @ApiModelProperty(value = "岗位显示进度条")
    private List<HmeSignInOutRecordDTO11> list;
}
