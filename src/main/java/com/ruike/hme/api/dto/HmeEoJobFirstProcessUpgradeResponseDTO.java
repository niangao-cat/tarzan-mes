package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoJobFirstProcessUpgradeResponseDTO
 * @Description PDA首序SN升级-基座条码扫描返回数据
 * @Date 2020/9/3 11:01
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobFirstProcessUpgradeResponseDTO implements Serializable {
    private static final long serialVersionUID = -1408112580190821192L;

    @ApiModelProperty(value = "EO ID")
    private String eoId;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "工艺路线步骤ID")
    private String routerStepId;
}