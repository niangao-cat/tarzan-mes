package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理任务单据
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Data
public class HmeEqManageTaskCheckVO implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "设备ID")
    private String equipmentId;
    @ApiModelProperty(value = "单据类型")
    private String docType;
    @ApiModelProperty(value = "周期")
    private String taskCycle;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "创建时间，年月日")
    private Date creationDate;
    @ApiModelProperty(value = "创建时间，年月日")
    private Date creationWeekDate;
    @ApiModelProperty(value = "创建时间，年月")
    private Date creationMonthDate;

}
