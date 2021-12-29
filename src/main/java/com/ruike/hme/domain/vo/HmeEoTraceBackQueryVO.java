package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEoTraceBackQueryVO
 *
 * @author:  chaonan.hu@hand-china.com 2020/08/12 16:46:23
 **/
@Data
public class HmeEoTraceBackQueryVO implements Serializable {
    private static final long serialVersionUID = 3566460802129683039L;

    @ApiModelProperty("物料序列号")
    private String materialLotCode;

    @ApiModelProperty("工单Id")
    private String workOrderId;

    @ApiModelProperty("工单编码")
    private String workOrderNum;

    @ApiModelProperty("eoId")
    private String eoId;

    @ApiModelProperty("eo编码")
    private String eoNum;

    @ApiModelProperty("投料时SN")
    private String feedSn;

    @ApiModelProperty("当前SN")
    private String currentSn;
}
