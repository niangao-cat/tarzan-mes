package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-08-04 15:23:07
 */
@Data
public class HmeEmployeeAttendanceDto5 implements Serializable {
    private static final long serialVersionUID = -1332139040986572149L;

    @ApiModelProperty(value = "站点Id")
    private String siteId;

    @ApiModelProperty(value = "工段Id")
    private String workId;

    @ApiModelProperty(value = "班组Id")
    private String unitId;

    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @ApiModelProperty(value = "班次日期")
    private LocalDate date;

    @ApiModelProperty(value = "班次日历Id")
    private String wkcShiftId;

    @ApiModelProperty(value = "班次开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftStartDate;

    @ApiModelProperty(value = "班次结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftEndDate;

    @ApiModelProperty(value = "工位名称")
    private String workcellName;

    @ApiModelProperty(value = "工单号")
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "员工批量查询")
    private String userId;

    @ApiModelProperty(value = "员工批量查询,后端自用")
    private List<String> userIdList;

    @ApiModelProperty(value = "产品编码批量查询")
    private String productCodeId;

    @ApiModelProperty(value = "产品编码批量查询,后端自用")
    private List<String> productCodeIdList;

    @ApiModelProperty(value = "BOM版本批量查询")
    private String bomVersion;

    @ApiModelProperty(value = "BOM版本批量查询,后端自用")
    private List<String> bomVersionList;
}
