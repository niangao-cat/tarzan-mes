package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname WmsCostCenterOrderQueryDTO
 * @Description 查询明细
 * @Date 2020/9/4 10:29
 * @Author yuchao.wang
 */
@Data
public class WmsCostCenterOrderQueryDTO implements Serializable {
    private static final long serialVersionUID = -3759373367058279798L;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料批状态")
    private String materialLotStatus;

    @ApiModelProperty(value = "单据行ID")
    private String instructionId;
}