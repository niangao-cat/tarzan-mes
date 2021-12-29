package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName AtpCollectItfDto
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/1/6 11:46
 * @Version 1.0
 **/
@Data
public class AtpCollectItfDTO implements Serializable {

    private static final long serialVersionUID = 3531149464043847315L;

    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "SN")
    private String sn;
    @ApiModelProperty(value = "HR-YB熔点温度")
    private BigDecimal atpHrYbRdt;
    @ApiModelProperty(value = "YB-OC熔点温度")
    private BigDecimal atpYbOcRdt;
    @ApiModelProperty(value = "OC-6+1熔点温度")
    private BigDecimal atpOc61Rdt;
    @ApiModelProperty(value = "HR-R6+1熔点温度")
    private BigDecimal atpHrR61Rdt;
    @ApiModelProperty(value = "6+1-CMS1熔点温度")
    private BigDecimal atp61Cms1Rdt;
    @ApiModelProperty(value = "CMS1-CMS2熔点温度")
    private BigDecimal atpCms1Cms2Rdt;
    @ApiModelProperty(value = "其他熔点熔点温度")
    private BigDecimal atpOtherRdt;
    @ApiModelProperty(value = "HR输入光纤温度")
    private BigDecimal atpHrSrgxt;
    @ApiModelProperty(value = "HR栅区温度")
    private BigDecimal atpHrSqt;
    @ApiModelProperty(value = "HR输出光纤温度")
    private BigDecimal atpHrScgxt;
    @ApiModelProperty(value = "OC输入光纤温度")
    private BigDecimal atpOcSrgxt;
    @ApiModelProperty(value = "OC栅区温度")
    private BigDecimal atpOcSqt;
    @ApiModelProperty(value = "OC输出光纤温度")
    private BigDecimal atpOcScgxt;
    @ApiModelProperty(value = "R6+1输入光纤温度")
    private BigDecimal atpR61Srgxt;
    @ApiModelProperty(value = "R6+1栅区温度")
    private BigDecimal atpR61Sqt;
    @ApiModelProperty(value = "R6+1输出光纤温度")
    private BigDecimal atpR61Scgxt;
    @ApiModelProperty(value = "6+1输入光纤温度")
    private BigDecimal atp61Srgxt;
    @ApiModelProperty(value = "6+1栅区温度")
    private BigDecimal atp61Sqt;
    @ApiModelProperty(value = "6+1输出光纤温度")
    private BigDecimal atp61Scgxt;
    @ApiModelProperty(value = "CMS1输入光纤温度")
    private BigDecimal atpCms1Srgxt;
    @ApiModelProperty(value = "CMS1栅区温度")
    private BigDecimal atpCms1Sqt;
    @ApiModelProperty(value = "CMS1输出光纤温度")
    private BigDecimal atpCms1Scgxt;
    @ApiModelProperty(value = "YB光纤盘温度")
    private BigDecimal atpYbGxpt;
    @ApiModelProperty(value = "其他器件温度")
    private BigDecimal atpOtherQjt;
    @ApiModelProperty(value = "红光功率")
    private BigDecimal atpRedPower;
    @ApiModelProperty(value = "红光电压")
    private BigDecimal atpRedVoltage;
    @ApiModelProperty(value = "监控点漏光值")
    private BigDecimal atpLeakage;
    @ApiModelProperty(value = "光谱图像")
    private String atpSpectralImage;
    @ApiModelProperty(value = "整机功率")
    private BigDecimal atpPower;
    @ApiModelProperty(value = "整机电流")
    private BigDecimal atpCurrent;
    @ApiModelProperty(value = "水冷机温度")
    private BigDecimal atpSlt;
    @ApiModelProperty(value = "操作者")
    private String atpOperator;
    @ApiModelProperty(value = "操作时间")
    private Date atpOperationTime;
    @ApiModelProperty(value = "")
    private String atpAttribute1;
    @ApiModelProperty(value = "")
    private String atpAttribute2;
    @ApiModelProperty(value = "")
    private String atpAttribute3;
    @ApiModelProperty(value = "")
    private String atpAttribute4;
    @ApiModelProperty(value = "")
    private String atpAttribute5;
    @ApiModelProperty(value = "")
    private String atpAttribute6;
    @ApiModelProperty(value = "")
    private String atpAttribute7;
    @ApiModelProperty(value = "")
    private String atpAttribute8;
    @ApiModelProperty(value = "")
    private String atpAttribute9;
    @ApiModelProperty(value = "")
    private String atpAttribute10;
    @ApiModelProperty(value = "")
    private String atpAttribute11;
    @ApiModelProperty(value = "")
    private String atpAttribute12;
    @ApiModelProperty(value = "")
    private String atpAttribute13;
    @ApiModelProperty(value = "")
    private String atpAttribute14;
    @ApiModelProperty(value = "")
    private String atpAttribute15;

    @ApiModelProperty(value = "HR-MOYB熔点温度")
    private BigDecimal atpHrMoybRdt;
    @ApiModelProperty(value = "MOYB-OC熔点温度")
    private BigDecimal atpMoybOcRdt;
    @ApiModelProperty(value = "OC-PAYB熔点温度")
    private BigDecimal atpOcPaybRdt;
    @ApiModelProperty(value = "PAYB-6+1熔点温度")
    private BigDecimal atpPayb61Rdt;
    @ApiModelProperty(value = "高反吸收块上温度")
    private BigDecimal atpBlockUT;
    @ApiModelProperty(value = "高反吸收块下温度")
    private BigDecimal atpBlockDT;
    @ApiModelProperty(value = "非线性数值")
    private BigDecimal atpNonLinear;
    @ApiModelProperty(value = "CMS-跳线熔点温度")
    private BigDecimal atpCmsJumperRdt;
    @ApiModelProperty(value = "监控点光纤1温度")
    private BigDecimal atpFiber1T;
    @ApiModelProperty(value = "监控点光纤2温度")
    private BigDecimal atpFiber2T;
    @ApiModelProperty(value = "CMS2输入光纤温度")
    private BigDecimal atpCms2Srgxt;
    @ApiModelProperty(value = "CMS2栅区温度")
    private BigDecimal atpCms2Sqt;
    @ApiModelProperty(value = "CMS2输出光纤温度")
    private BigDecimal atpCms2Scgxt;
    @ApiModelProperty(value = "环境温度")
    private BigDecimal atpTemperature;
    @ApiModelProperty(value = "环境湿度")
    private BigDecimal atpHumidity;
    @ApiModelProperty(value = "光学壳体温度")
    private BigDecimal atpCaseT;
}
