package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeEquipmentStocktakePdaVO2
 * 扫描设备返回对象VO
 * @author: chaonan.hu@hand-china.com 2021/04/01 16:22:45
 **/
@Data
public class HmeEquipmentStocktakePdaVO2 implements Serializable {
    private static final long serialVersionUID = -4355895774502455101L;

    @ApiModelProperty(value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "机身序列号")
    private String equipmentBodyNum;

    @ApiModelProperty(value = "存放位置")
    private String location;

    @ApiModelProperty(value = "设备状态")
    private String equipmentStatus;

    @ApiModelProperty(value = "设备状态含义")
    private String equipmentStatusMeaning;

    @ApiModelProperty(value = "盘点结果")
    private String result;

    @ApiModelProperty(value = "盘点结果颜色")
    private String resultColor;

    @ApiModelProperty(value = "盘点实绩ID")
    private String stocktakeActualId;

    @ApiModelProperty(value = "盘点时间")
    private Date stocktakeDate;

    @ApiModelProperty(value = "提交按钮颜色")
    private String submitButtonColor;

    @ApiModelProperty(value = "保管部门ID")
    private String businessId;

    @ApiModelProperty(value = "保管部门")
    private String businessName;

    @ApiModelProperty(value = "规格型号")
    private String model;
}
