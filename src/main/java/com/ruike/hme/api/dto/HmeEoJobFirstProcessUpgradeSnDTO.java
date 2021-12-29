package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoJobFirstProcessUpgradeSnDTO
 * @Description PDA首序SN升级-SN升级参数
 * @Date 2020/9/3 11:01
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobFirstProcessUpgradeSnDTO implements Serializable {
    private static final long serialVersionUID = 2745834519786845296L;

    @ApiModelProperty(value = "SN编码")
    private String snNum;

    @ApiModelProperty(value = "EO ID")
    private String eoId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "底座物料批id")
    private String baseMaterialLotId;

    @ApiModelProperty(value = "工艺路线步骤ID")
    private String routerStepId;
}