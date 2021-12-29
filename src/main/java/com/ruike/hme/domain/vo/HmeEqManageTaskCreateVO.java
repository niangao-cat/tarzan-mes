package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理任务单据创建
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Data
public class HmeEqManageTaskCreateVO implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "项目组ID")
    private String manageTagGroupId;
    @ApiModelProperty(value = "班次ID")
    private String calendarShiftId;
    @ApiModelProperty(value = "周期")
    private String cycle;
    @ApiModelProperty(value = "单据类型")
    private String docType;

}
