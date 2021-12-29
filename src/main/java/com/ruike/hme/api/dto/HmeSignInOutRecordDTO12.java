package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工出勤报表 输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/05 13:47
 */
@Data
public class HmeSignInOutRecordDTO12 implements Serializable {

    private static final long serialVersionUID = 9141855801887113404L;

    @ApiModelProperty(value = "部门")
    private String departmentId;

    @ApiModelProperty(value = "产线")
    private String prodLineId;

    @ApiModelProperty(value = "员工")
    private String employeeNum;

    @ApiModelProperty(value = "工段")
    private String workcellId;

    @ApiModelProperty(value = "开始日期")
    private String dateFrom;

    @ApiModelProperty(value = "截至日期")
    private String dateTo;
}
