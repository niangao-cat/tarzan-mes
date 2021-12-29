package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * fac数据接口接收返回DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/4 19:59
 */
@Data
public class FacCollectItfDTO {
    //exchenge by wenzhnag 2002.08.11 在字段前加设备类
    //exchenge by wenzhnag 2002.08.11 取消全两个字段设备类
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "FAC批次")
    private String facLot;
    @ApiModelProperty(value = "胶水批次")
    private String facGlueLot;
    @ApiModelProperty(value = "胶水类型")
    private String facGlueType;
    @ApiModelProperty(value = "胶水打开时间")
    private Date facGlueOpenDate;
    @ApiModelProperty(value = "烘烤开始时间")
    private Date facHotStartDate;
    @ApiModelProperty(value = "是否存图")
    private String facIsChart;
    @ApiModelProperty(value = "COS路数")
    private String facCosPos;
    @ApiModelProperty(value = "电流")
    private BigDecimal facCurrent;
    @ApiModelProperty(value = "质心Y")
    private BigDecimal facCenterY;
    @ApiModelProperty(value = "质心X")
    private BigDecimal facCenterX;
    @ApiModelProperty(value = "Y宽")
    private BigDecimal facWidthY;
    @ApiModelProperty(value = "X宽")
    private BigDecimal facWidthX;
    @ApiModelProperty(value = "不良类型")
    private String facErr;
    @ApiModelProperty(value = "备注")
    private String facRemark;
    @ApiModelProperty(value = "COS类型")
    private String facCosType;
    @ApiModelProperty(value = "平行光偏移")
    private String facParaShift;
    @ApiModelProperty(value = "光斑角度")
    private String facRayAngle;
    @ApiModelProperty(value = "实验代码")
    private String facExpCode;

    @ApiModelProperty(value = "COS不良")
    private String facCosNcCode;
    @ApiModelProperty(value = "器件不良")
    private String facNcCode;

    @ApiModelProperty(value = "")
    private String facAttribute1;
    @ApiModelProperty(value = "")
    private String facAttribute2;
    @ApiModelProperty(value = "")
    private String facAttribute3;
    @ApiModelProperty(value = "")
    private String facAttribute4;
    @ApiModelProperty(value = "")
    private String facAttribute5;
    @ApiModelProperty(value = "")
    private String facAttribute6;
    @ApiModelProperty(value = "")
    private String facAttribute7;
    @ApiModelProperty(value = "")
    private String facAttribute8;
    @ApiModelProperty(value = "")
    private String facAttribute9;
    @ApiModelProperty(value = "")
    private String facAttribute10;
    @ApiModelProperty(value = "")
    private String facAttribute11;
    @ApiModelProperty(value = "")
    private String facAttribute12;
    @ApiModelProperty(value = "")
    private String facAttribute13;
    @ApiModelProperty(value = "")
    private String facAttribute14;
    @ApiModelProperty(value = "")
    private String facAttribute15;
}
