package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

/**
 * @Classname HmeEoJobReleaseMaterial
 * @Description 投料数量视图
 * @Date 2020/11/22 16:09
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobReleaseMaterialVO implements Serializable {
    private static final long serialVersionUID = -1685894719585035622L;

    @ApiModelProperty(value = "作业ID")
    private String jobId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "投料数量")
    private BigDecimal releaseQty;

    @ApiModelProperty(value = "物料类型")
    private String materialType;
}