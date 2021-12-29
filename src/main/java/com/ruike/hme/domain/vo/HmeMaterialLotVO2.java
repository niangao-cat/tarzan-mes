package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeMaterialLotVO2
 * @Description 物料批LOV返回数据
 * @Date 2020/9/1 19:21
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotVO2 implements Serializable {
    private static final long serialVersionUID = -5840647723696164383L;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private Long primaryUomQty;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;
}