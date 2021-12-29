package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname HmeMaterialLotVO
 * @Description 物料批信息视图
 * @Date 2020/8/25 9:19
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotVO implements Serializable {
    private static final long serialVersionUID = -5722336009462678438L;

    @ApiModelProperty(value = "eoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private Long primaryUomQty;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良描述")
    private String ncDesc;

    @ApiModelProperty(value = "是否不良")
    private String ncFlag;

    @ApiModelProperty(value = "是否打印")
    private String printFlag;

    @ApiModelProperty(value = "在制标识")
    private String mfFlag;

    @ApiModelProperty(value = "实验代码")
    private String labCode;
}