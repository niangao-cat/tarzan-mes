package com.ruike.hme.domain.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/12 10:50
 */
@Data
public class HmeSelfMadeRepairVO implements Serializable {

    private static final long serialVersionUID = -8022305422484220770L;

    @ApiModelProperty(value = "原条码")
    private String sourceMaterialLotCode;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "工单版本")
    private String version;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "EO主键")
    private String eoId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "EO编码")
    private String reworkMaterialLot;

    @ApiModelProperty(value = "是否售后")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "afFlagMeaning")
    private String afFlag;

    @ApiModelProperty(value = "是否售后含义")
    private String afFlagMeaning;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "产线")
    private String productionLineId;

    @ApiModelProperty(value = "建议SN")
    private String suggestSnCode;

    @ApiModelProperty(value = "建议SN-组成")
    private List<String> suggestSnCodeList;

    @ApiModelProperty(value = "站点及产线简码")
    private String siteLine;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "年月")
    private String yearMonth;

    @ApiModelProperty(value = "转化方式 1-建议 2-自定义")
    private String transformMode;
}
