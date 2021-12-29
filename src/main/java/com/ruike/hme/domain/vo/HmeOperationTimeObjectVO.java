package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

/**
 * @Classname HmeOperationTimeObjectVO
 * @Description 时效时长
 * @Date 2020/10/21 21:08
 * @Author yuchao.wang
 */
@Data
public class HmeOperationTimeObjectVO implements Serializable {
    private static final long serialVersionUID = -6394068952704164892L;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "eoId")
    private String eoId;
}