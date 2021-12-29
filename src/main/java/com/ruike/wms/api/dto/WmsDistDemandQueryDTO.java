package com.ruike.wms.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 配送需求平台查询传入参数
 *
 * @author penglin.sui@hand-china.com 2020/07/21 17:21
 */
@ApiModel("配送需求平台查询传入参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WmsDistDemandQueryDTO implements Serializable {
    private static final long serialVersionUID = 1918568501240680565L;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "组件物料ID")
    private String materialId;
    @ApiModelProperty(value = "工段ID")
    private String workcellId;
    @ApiModelProperty(value = "主件物料ID")
    private String mainMaterialId;
    @ApiModelProperty(value = "策略")
    private String distributionType;
    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    @ApiModelProperty(value = "结束时间")
    private Date endDate;
    @ApiModelProperty(value = "未生成配送单")
    private String docNotCreatedFlag;
    @ApiModelProperty(value = "只看需要配送的数据")
    private String onlyDistributionFlag;
}