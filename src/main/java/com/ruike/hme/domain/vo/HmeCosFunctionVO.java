package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

/**
 * @Classname HmeCosFunctionVO
 * @Description 芯片性能表视图
 * @Date 2020/9/30 18:13
 * @Author yuchao.wang
 */
@Data
public class HmeCosFunctionVO implements Serializable {
    private static final long serialVersionUID = 2807630756720606370L;

    @ApiModelProperty(value = "新盒位置")
    private String newLoad;

    @ApiModelProperty(value = "COS序号")
    private String cosNum;

    @ApiModelProperty(value = "新盒位置-行号")
    private Long newLoadRow;

    @ApiModelProperty(value = "新盒位置-列号")
    private Long newLoadColumn;

    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String cosFunctionId;
    @ApiModelProperty(value = "芯片序列号", required = true)
    private String loadSequence;
    @ApiModelProperty(value = "组织id", required = true)
    private String siteId;
    @ApiModelProperty(value = "电流", required = true)
    private String current;
    @ApiModelProperty(value = "功率等级")
    private String a01;
    @ApiModelProperty(value = "功率/w")
    private BigDecimal a02;
    @ApiModelProperty(value = "波长等级")
    private String a03;
    @ApiModelProperty(value = "波长/nm")
    private BigDecimal a04;
    @ApiModelProperty(value = "波长差/nm")
    private BigDecimal a05;
    @ApiModelProperty(value = "电压/V")
    private BigDecimal a06;
    @ApiModelProperty(value = "光谱宽度(单点)")
    private BigDecimal a07;
    @ApiModelProperty(value = "")
    private String a08;
    @ApiModelProperty(value = "")
    private String a09;
    @ApiModelProperty(value = "")
    private BigDecimal a010;
    @ApiModelProperty(value = "")
    private BigDecimal a011;
    @ApiModelProperty(value = "")
    private BigDecimal a012;
    @ApiModelProperty(value = "")
    private BigDecimal a013;
    @ApiModelProperty(value = "")
    private BigDecimal a014;
    @ApiModelProperty(value = "偏振度数")
    private BigDecimal a015;
    @ApiModelProperty(value = "X半宽高")
    private BigDecimal a016;
    @ApiModelProperty(value = "Y半宽高")
    private BigDecimal a017;
    @ApiModelProperty(value = "X86能量宽度")
    private BigDecimal a018;
    @ApiModelProperty(value = "Y86能量宽度")
    private BigDecimal a019;
    @ApiModelProperty(value = "X95能量宽度")
    private BigDecimal a020;
    @ApiModelProperty(value = "Y95能量宽度")
    private BigDecimal a021;
    @ApiModelProperty(value = "透镜功率")
    private BigDecimal a022;
    @ApiModelProperty(value = "PBS功率")
    private BigDecimal a023;
    @ApiModelProperty(value = "不良代码")
    private String a024;
    @ApiModelProperty(value = "操作者")
    private String a025;
    @ApiModelProperty(value = "备注")
    private String a026;
    @ApiModelProperty(value = "COS路数")
    private String facCosPos;
}