package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 工单派工工单信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/2 13:48
 */
@Data
public class HmeWoDispatchWorkOrderVO {
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "交付时间")
    private String deliveryDate;

    @ApiModelProperty(value = "派工明细")
    private List<HmeWoDispatchWkcVO> detailList;
}
