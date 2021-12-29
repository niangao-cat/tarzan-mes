package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */

@Data
public class HmeWoDispatchVO2 implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "交付时间")
    private String deliveryDate;
    @ApiModelProperty(value = "产品线ID")
    private String prodLineId;
    @ApiModelProperty(value = "产品线名称")
    private String prodLineName;
    @ApiModelProperty(value = "产品线说明")
    private String prodLineDesc;

    private List<HmeWoDispatchVO3> woWorkCellList;

}
