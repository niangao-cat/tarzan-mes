package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author li.zhang 2021/01/14 14:15
 */
@Data
public class HmeEquipmentWorkingHisVO {

    @ApiModelProperty("事件型描述")
    private String eventTypeCode;

    @ApiModelProperty("时间")
    private String creationDate;

}
