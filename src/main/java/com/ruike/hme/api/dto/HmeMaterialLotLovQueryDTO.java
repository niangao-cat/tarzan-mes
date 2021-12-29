package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeMaterialLotLovQueryDTO
 * @Description 物料批LOV查询DTO
 * @Date 2020/9/1 14:14
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotLovQueryDTO implements Serializable {
    private static final long serialVersionUID = -2353247092300350262L;

    @ApiModelProperty(value = "容器条码")
    private String barode;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;
}