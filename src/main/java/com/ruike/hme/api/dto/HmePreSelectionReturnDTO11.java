package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: tarzan-mes->HmePreSelectionReturnDTO11
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/01/12 14:58:43
 **/
@Data
public class HmePreSelectionReturnDTO11 implements Serializable {
    private static final long serialVersionUID = -8577779218123117486L;

    @ApiModelProperty(value = "挑选批次")
    private String selectLot;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "原盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "目标盒子号")
    private String newMaterialLotCode;

    @ApiModelProperty(value = "挑选时间从")
    private Date creationDateFrom;

    @ApiModelProperty(value = "挑选时间至")
    private Date creationDateTo;
}
