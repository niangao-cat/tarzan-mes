package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname HmeEoVO3
 * @Description 执行作业管理-打印
 * @Date 2020/9/1 14:14
 * @Author yaoyapeng
 */
@Data
public class HmeEoVO3 implements Serializable {
    private static final long serialVersionUID = -7311738167681883414L;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "生产指令编码")
    private String workOrder;

    @ApiModelProperty(value = "生产版本")
    private String version;

    @ApiModelProperty(value = "销售订单")
    private String soNum;

    @ApiModelProperty(value = "工单号")
    private String workOrderId;

    @ApiModelProperty(value = "选项值")
    private String printOptionValue;
}
