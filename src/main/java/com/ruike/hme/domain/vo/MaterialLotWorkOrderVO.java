package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 物料批工单信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 14:44
 */
@Data
public class MaterialLotWorkOrderVO {
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
}
