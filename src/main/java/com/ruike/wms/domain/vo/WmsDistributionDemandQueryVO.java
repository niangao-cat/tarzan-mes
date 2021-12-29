package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 配送需求查询
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 14:13
 */
@Data
public class WmsDistributionDemandQueryVO {
    @ApiModelProperty(value = "日历ID")
    private String calendarId;
    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    @ApiModelProperty(value = "结束时间")
    private Date endDate;
    @ApiModelProperty(value = "组件物料ID")
    private String materialId;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "工厂")
    private String siteId;
    @ApiModelProperty(value = "产品版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "工段（工作单元）ID")
    private String workcellId;
    @ApiModelProperty(value = "策略类型")
    private String distributionType;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
}
