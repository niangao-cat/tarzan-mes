package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工单回传ERP专用VO
 *
 * @author kejin.liu01@hand-china.com 2020/8/27 10:31
 */
@Data
public class ItfWoStatusSendErp {

    @ApiModelProperty(value = "工厂编码", required = true)
    private String plantCode;

    @ApiModelProperty(value = "工单编码", required = true)
    private String workOrderNum;

    @ApiModelProperty(value = "工单状态", required = true)
    private String woStatus;

    @ApiModelProperty(value = "工单标示", required = true)
    private String activateFlag;

    @ApiModelProperty(value = "最后修改时间", required = false)
    private Date lastUpdateDate;
}
