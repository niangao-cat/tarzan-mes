package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoJobSnVO8
 * @Description 首序SN升级出站
 * @Date 2020/9/3 17:24
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobSnVO8 implements Serializable {
    private static final long serialVersionUID = 3885841400455134407L;

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