package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * @Classname MiscInBarCodeDTO
 * @Description 杂收 扫描条码 查询数据DTO
 * @Date 2019/9/26 18:10
 * @Author zhihao.sang
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscInBarCodeDTO {

    @ApiModelProperty("成本中心ID(杂收确认时，接收前端传入参数)")
    private String costCenterId;

    @ApiModelProperty("成本中心(杂收确认时，接收前端传入参数)")
    private String costCenterCode;

    //为了去分特殊成本中心，true代表特殊成本中心

    @ApiModelProperty("成本中心状态")
    private String costCenterState;

    @ApiModelProperty("物料批ID(隐藏)")
    private String materialLotId;

    @ApiModelProperty("条码")
    private String barCode;

    @ApiModelProperty("货位ID(隐藏)")
    private String locatorId;

    @ApiModelProperty("货位编码")
    private String locatorCode;

    @ApiModelProperty("工厂ID(隐藏)")
    private String siteId;

    @ApiModelProperty("工厂")
    private String plant;

    @ApiModelProperty("物料ID(隐藏)")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("单位ID(隐藏)")
    private String uomId;

    @ApiModelProperty("数量")
    private Double qty;

    @ApiModelProperty("杂收数量")
    private Double miscInQty;

    @ApiModelProperty("变更后数量")
    private Double qtyAfter;

    @ApiModelProperty("条码状态")
    private String status;

    @ApiModelProperty("条码状态含义")
    private String statusMeaning;

    @ApiModelProperty("质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty("质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty("有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty("有效性含义")
    private String enableFlagMeaning;

    @ApiModelProperty("批次(隐藏)")
    private String lot;

    @ApiModelProperty("返回数据的状态信息")
    private String returnMessage;

    @ApiModelProperty(value = "ERP标识")
    private String mergeFlag;

    @ApiModelProperty(value = "SAP_ACCOUNT_FLAG标识")
    private String sapAccountFlag;

    @ApiModelProperty(value = "物料类型")
    private String itemType;
}
