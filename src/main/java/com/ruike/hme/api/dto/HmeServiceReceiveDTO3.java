package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeServiceReceiveDTO3
 *
 * @author: chaonan.hu@hand-china.com 2020/9/1 16:06:29
 **/
@Data
public class HmeServiceReceiveDTO3 implements Serializable {
    private static final long serialVersionUID = -8326334844909661143L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "SN", required = true)
    private String snNum;

    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;

    @ApiModelProperty(value = "售后机器部门", required = true)
    private String areaCode;
}
