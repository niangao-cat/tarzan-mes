package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 派工组件齐套信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/14 11:07
 */
@Data
public class HmeWoDispatchComponentSuiteVO {
    @ApiModelProperty(value = "路线ID")
    private String workcellId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "组件物料ID")
    private String materialId;
    @ApiModelProperty(value = "组件物料编码")
    private String materialCode;
    @ApiModelProperty(value = "组件物料名称")
    private String materialName;
    @ApiModelProperty(value = "组件物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "单位用量")
    private BigDecimal usageQty;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "仓库现有量")
    private BigDecimal inStockQty;
    @ApiModelProperty(value = "组件齐套数")
    private Long suiteQty;
    @ApiModelProperty(value = "线边库存")
    private BigDecimal workcellQty;
    @ApiModelProperty(value = "线边套数")
    private Long workcellSuiteQty;
}
