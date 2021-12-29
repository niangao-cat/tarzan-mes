package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCosPatchPdaVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/8/25 18:18:39
 **/
@Data
public class HmeCosPatchPdaVO2 implements Serializable {
    private static final long serialVersionUID = -8863493196509557930L;

    @ApiModelProperty("序号")
    private Long number;

    @ApiModelProperty("作业记录Id，用于打印按钮的后台逻辑")
    private String jobId;

    @ApiModelProperty("物料批Id，单纯用于打印条码")
    private String materialLotId;

    @ApiModelProperty("物料批条码")
    private String materialLotCode;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("实验代码")
    private String labCode;
}
