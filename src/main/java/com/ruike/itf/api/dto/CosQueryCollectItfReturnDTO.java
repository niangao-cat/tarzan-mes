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
public class CosQueryCollectItfReturnDTO implements Serializable {
    private static final long serialVersionUID = 5463368338567026363L;
    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "芯片数量")
    private String cosQuantity;

    @ApiModelProperty(value = "工单号")
    private String cosWo;

    @ApiModelProperty(value = "工单数量")
    private String woQuantity;

    @ApiModelProperty(value = "物料编码")
    private String  materialCode;

    @ApiModelProperty(value = "物料描述")
    private String  materialDescription;

    @ApiModelProperty(value = "结果")
    private List<CosQueryCollectItfReturnDTO1> resultList;

    @ApiModelProperty(value = "处理消息")
    private String processMessage;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;

}
