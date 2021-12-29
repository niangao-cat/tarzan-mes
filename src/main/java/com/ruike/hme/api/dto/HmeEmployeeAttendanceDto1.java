package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-27 11:28:07
 */
@Data
public class HmeEmployeeAttendanceDto1 implements Serializable {
    @ApiModelProperty(value = "工厂id",required = true)
    private String  siteId;
    @ApiModelProperty(value = "事业部id")
    private String  areaId;
    @ApiModelProperty(value = "车间id")
    private String  workshopId;
    @ApiModelProperty(value = "产线id")
    private String productionLineId ;
    @ApiModelProperty(value = "工段")
    private String lineWorkcellId;
    @ApiModelProperty(value = "日期开始",required = true)
    private Date startTime;
    @ApiModelProperty(value = "日期截止",required = true)
    private Date endTime;
    @ApiModelProperty(value = "班组id")
    private String relId;

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

    @ApiModelProperty(value = "工段id")
    private List<String> lineWorkcellIdList;
}
