package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */

@Data
public class HmeWoDispatchVO5 implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;

    @ApiModelProperty(value = "产品ID")
    private String productId;
    @ApiModelProperty(value = "产品编码")
    private String productCode;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "合计列")
    private String total;

    List<HmeWoCalendarShiftVO> hmeCalendarShiftList;
}
