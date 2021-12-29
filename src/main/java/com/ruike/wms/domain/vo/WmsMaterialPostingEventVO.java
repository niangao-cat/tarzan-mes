package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 物料过账事件VO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/17 15:11
 */
@Data
@AllArgsConstructor
public class WmsMaterialPostingEventVO {
    @ApiModelProperty("事件请求ID")
    private String eventRequestId;
    @ApiModelProperty("调换OUT事件ID")
    private String exchangeOutEventId;
    @ApiModelProperty("调换IN事件ID")
    private String exchangeInEventId;
}
