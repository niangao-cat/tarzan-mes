package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEquipmentVO4 implements Serializable {
    private static final long serialVersionUID = -4736512636207818495L;
    @ApiModelProperty(value = "工序作业ID")
    private String jobId;
    @ApiModelProperty(value = "设备名称")
    private String assetName;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
}
