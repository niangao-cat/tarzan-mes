package com.ruike.wms.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 配送需求平台查询传出参数
 *
 * @author penglin.sui@hand-china.com 2020/07/22 11:46
 */

@ApiModel("配送需求平台查询传出参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WmsDistributionDemandDetailResponseDTO implements Serializable {
    private static final long serialVersionUID = 6623607703883496244L;
    @ApiModelProperty(value = "工单-工段派工ID")
    private String woDispatchId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂")
    private String siteCode;
    @ApiModelProperty(value = "产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "产线")
    private String prodLineCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @LovValue(lovCode = "WMS.DISTRIBUTION", meaningField = "materialVersion", defaultMeaning = "无")
    @ApiModelProperty(value = "配送策略")
    private String distributionType;
    @ApiModelProperty(value = "销售订单号")
    private String saleOrderNum;
    @ApiModelProperty(value = "销售订单行号")
    private String saleOrderLine;
    @ApiModelProperty(value = "替代料")
    private String subsititutionMaterialCodes;
    @ApiModelProperty(value = "替代数量")
    private String subsititutionQtys;
    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "总需求数量")
    private Double requirementQty;
    @ApiModelProperty(value = "已配送数量")
    private Double coverQty;
    @ApiModelProperty(value = "目前线边库存")
    private Double inventory;
    @ApiModelProperty(value = "剩余配送数量")
    private Double remainQty;
}
