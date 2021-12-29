package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工单信息
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 20:24
 */
@Data
public class HmeWoInputRecordDTO implements Serializable {

    private static final long serialVersionUID = -355498152391006227L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;
    @ApiModelProperty(value = "工单类型")
    private String workOrderType;
    @ApiModelProperty(value = "工单类型说明")
    private String woType;
    @ApiModelProperty(value = "工单状态")
    private String status;
    @ApiModelProperty(value = "工单状态说明")
    private String woStatus;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "工单数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "BOM ID")
    private String bomId;
    @ApiModelProperty(value = "BOM名称")
    private String bomName;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "工艺路线编码")
    private String routerName;
    @ApiModelProperty(value = "完工工位ID")
    private String locatorId;
    @ApiModelProperty(value = "完工工位编码")
    private String locatorCode;
    @ApiModelProperty(value = "返修标识")
    private Boolean reworkFlag;

    @ApiModelProperty(value = "装配清单")
    List<HmeWoInputRecordDTO4> dtoList;
}
