package com.ruike.hme.api.dto;

import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class HmeEmployeeAttendanceDTO15 implements Serializable {


    @ApiModelProperty(value = "工段id")
    private String workId;

    @ApiModelProperty(value = "班次日期")
    private String date ;

    @ApiModelProperty(value = "班次Id")
    private String wkcShiftId;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "工单号")
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "产品编码批量查询")
    private String productCodeId;

    @ApiModelProperty(value = "产品编码批量查询,后端自用")
    private List<String> productCodeIdList;

    @ApiModelProperty(value = "BOM版本批量查询")
    private String bomVersion;

    @ApiModelProperty(value = "BOM版本批量查询,后端自用")
    private List<String> bomVersionList;

    @ApiModelProperty(value = "员工批量查询")
    private String userId;

    @ApiModelProperty(value = "员工批量查询,后端自用")
    private List<String> userIdList;

    @ApiModelProperty(value = "班次结束时间")
    private String shiftEndDate;

    @ApiModelProperty(value = "班次开始时间")
    private String shiftStartDate;

    public void initParam() {
        userIdList = StringUtils.isNotBlank(userId) ? Arrays.asList(StringUtils.split(userId, ",")) : null;
        bomVersionList = StringUtils.isNotBlank(bomVersion) ? Arrays.asList(StringUtils.split(bomVersion, ",")) : null;
        productCodeIdList = StringUtils.isNotBlank(productCodeId) ? Arrays.asList(StringUtils.split(productCodeId, ",")) : null;
    }
}
