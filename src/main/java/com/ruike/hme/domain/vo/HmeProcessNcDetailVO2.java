package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

/**
 * @Classname HmeProcessNcDetailVO2
 * @Description HmeProcessNcDetailVO2
 * @Date 2021/1/22 17:34
 * @Author yuchao.wang
 */
@Data
public class HmeProcessNcDetailVO2 implements Serializable {
    private static final long serialVersionUID = 3953365345683994209L;

    @ApiModelProperty("工序作业ID")
    private String jobId;

    @ApiModelProperty("明细表ID")
    private String detailId;

    @ApiModelProperty("最大值")
    private BigDecimal maxValue;

    @ApiModelProperty("最小值")
    private BigDecimal minValue;

    @ApiModelProperty("不良代码组ID")
    private String ncGroupId;

    @ApiModelProperty("不良代码ID")
    private String ncCodeId;

    @ApiModelProperty("不良代码编码")
    private String ncCode;

    @ApiModelProperty("不良代码描述")
    private String ncDescription;

    @ApiModelProperty("标准编码")
    private String detailStandardCode;

    @ApiModelProperty("返修标识")
    private String reworkFlag;

    @ApiModelProperty("降级标识")
    private String downgradeFlag;

    @ApiModelProperty("不良数据项")
    private String tagId;

    @ApiModelProperty("COS路数")
    private String cosCode;

    @ApiModelProperty("COS路数拼接值")
    private String spliceCosCode;
}