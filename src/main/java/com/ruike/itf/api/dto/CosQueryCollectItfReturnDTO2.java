package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SnQueryCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:34
 * @Version 1.0
 **/
@Data
public class CosQueryCollectItfReturnDTO2 implements Serializable {
    private static final long serialVersionUID = -610326684536107730L;

    @ApiModelProperty(value = "盒子号Id")
    private String materialLotId;

    @ApiModelProperty(value = "芯片数量")
    private String primaryUomQty;

    @ApiModelProperty(value = "物料编码")
    private String  materialCode;

    @ApiModelProperty(value = "物料描述")
    private String  materialName;

    @ApiModelProperty(value = "工单")
    private String  workOrderNum;

    @ApiModelProperty(value = "工单数量")
    private String  qty;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private String wafer;
}
