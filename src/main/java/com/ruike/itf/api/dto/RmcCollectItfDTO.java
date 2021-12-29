package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 反射镜数据接口接收返回DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/4 20:01
 */
@Data
public class RmcCollectItfDTO {
    //exchenge by wenzhnag 2002.08.11 在字段前加设备类
    //exchenge by wenzhnag 2002.08.11 取消全两个字段设备类
    @ApiModelProperty(value = "设备资产编码")
    //private String rmcAssetEncoding;
    private String assetEncoding;
    @ApiModelProperty(value = "产品SN")
    //private String rmcSn;
    private String sn;
    @ApiModelProperty(value = "反射镜批次")
    private String rmcLot;
    @ApiModelProperty(value = "胶水批次")
    private String rmcGlueLot;
    @ApiModelProperty(value = "胶水型号")
    private String rmcGlueType;
    @ApiModelProperty(value = "胶水打开时间")
    private Date rmcGlueOpenDate;
    @ApiModelProperty(value = "烘烤开始时间")
    private Date rmcHotStartDate;
    @ApiModelProperty(value = "烘烤结束时间")
    private Date rmcHotEndDate;
    @ApiModelProperty(value = "COS路数")
    private String rmcCosPos;
    @ApiModelProperty(value = "电流")
    private BigDecimal rmcCurrent;
    @ApiModelProperty(value = "空对功率",required = true)
    private BigDecimal rmcAirToPower;
    @ApiModelProperty(value = "固化前功率",required = true)
    private BigDecimal rmcBeforeCuringPower;
    @ApiModelProperty(value = "固化后功率",required = true)
    private BigDecimal rmcAfterCuringPower;
    @ApiModelProperty(value = "耦合效率")
    private BigDecimal rmcCouplingEfficiency;
    @ApiModelProperty(value = "不良类型")
    private String rmcErr;
    @ApiModelProperty(value = "备注")
    private String rmcRemark;
    @ApiModelProperty(value = "工序状态")
    private String rmcProStatus;
    @ApiModelProperty(value = "COS类型")
    private String rmcCosType;
    @ApiModelProperty(value = "实验代码")
    private String rmcExpCode;

    @ApiModelProperty(value = "COS不良")
    private String rmcCosNcCode;
    @ApiModelProperty(value = "器件不良")
    private String rmcNcCode;

    @ApiModelProperty(value = "")
    private String rmcAttribute1;
    @ApiModelProperty(value = "")
    private String rmcAttribute2;
    @ApiModelProperty(value = "")
    private String rmcAttribute3;
    @ApiModelProperty(value = "")
    private String rmcAttribute4;
    @ApiModelProperty(value = "")
    private String rmcAttribute5;
    @ApiModelProperty(value = "")
    private String rmcAttribute6;
    @ApiModelProperty(value = "")
    private String rmcAttribute7;
    @ApiModelProperty(value = "")
    private String rmcAttribute8;
    @ApiModelProperty(value = "")
    private String rmcAttribute9;
    @ApiModelProperty(value = "")
    private String rmcAttribute10;
    @ApiModelProperty(value = "")
    private String rmcAttribute11;
    @ApiModelProperty(value = "")
    private String rmcAttribute12;
    @ApiModelProperty(value = "")
    private String rmcAttribute13;
    @ApiModelProperty(value = "")
    private String rmcAttribute14;
    @ApiModelProperty(value = "")
    private String rmcAttribute15;
}
