package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

/**
 * @Classname HmeEoVO
 * @Description EO LOV返回数据
 * @Date 2020/9/1 14:14
 * @Author yuchao.wang
 */
@Data
public class HmeEoVO implements Serializable {
    private static final long serialVersionUID = -2860721523783714365L;

    @ApiModelProperty(value = "EO ID")
    private String eoId;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "标识说明")
    private String identification;

    @ApiModelProperty(value = "首序进站标识")
    private String firstStepInFlag;
}
