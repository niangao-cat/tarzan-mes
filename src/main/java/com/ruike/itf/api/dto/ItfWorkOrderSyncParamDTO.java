package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfWorkOrderSyncParamDTO {

    @ApiModelProperty(value = "工单")
    List<ItfWorkOrderSyncDTO> workOrderList;
    @ApiModelProperty(value = "bom")
    List<ItfBomComponentSyncDTO> bomComponentList;
    @ApiModelProperty(value = "工艺路线")
    List<ItfRoutingOperationSyncDTO> routingOperationList;

}
