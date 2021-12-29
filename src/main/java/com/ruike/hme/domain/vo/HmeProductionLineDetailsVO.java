package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeProductionLineDetailsVO
 *
 * @author bao.xu@hand-china.com 2020-07-07 12:10:20
 */

@Data
public class HmeProductionLineDetailsVO implements Serializable {

    private static final long serialVersionUID = -7859268629835359413L;

    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "站点")
    private String siteId;
    @ApiModelProperty(value = "车间")
    private String parentOrganizationId;
    @ApiModelProperty(value = "生产线")
    private String productionLineId;
    @ApiModelProperty(value = "生产线列表")
    private List<String> productionLineIds;
    @ApiModelProperty(value = "工位")
    private String workcellId;
    @ApiModelProperty(value = "产品")
    private String materialId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "班次id")
    private String shiftId;
    @ApiModelProperty(value = "班次时间")
    private String shiftDate;
}
